package com.example.samsung.p1011_contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.net.URI;

public class MyContentProvider extends ContentProvider {

    //Uri:
    static final String
    //  - authority
        AUTHORITY = "com.example.samsung.p1011_contentprovider.AddressBook",
    //  - path
        CONTACT_PATH = DB.getTableContact();
    //  - general uri
    public static final Uri CONTACT_CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/" + CONTACT_PATH
    );
    //Data type:
    static final String
    //  - strings set
        CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CONTACT_PATH,
    //  - one string
        CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + CONTACT_PATH;
    //UriMatcher:
    static final int
    //  - general uri
        URI_CONTACTS = 1,
    //  - ori with id
        URI_CONTACTS_ID = 2;
    //Description and creation of the UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_CONTACTS);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH + "/#", URI_CONTACTS_ID);
    }

    DB database;

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String message = "MyContentProvider delete(): " + uri.toString();
        Messager.sendMessageToAllRecipients(getContext(), message);

        switch (uriMatcher.match(uri)) {

            case URI_CONTACTS :
                message = "-------------------------> URI_CONTACTS";
                Messager.sendMessageToAllRecipients(getContext(), message);
                break;
            case URI_CONTACTS_ID :
                String id = uri.getLastPathSegment();
                message = "-------------------------> URI_CONTACTS_ID = " + id;
                Messager.sendMessageToAllRecipients(getContext(), message);
                if (TextUtils.isEmpty(selection)) {
                    selection = DB.getTableId() + " = " + id;
                } else {
                    selection += (" AND " + DB.getTableId() + " = " + id);
                }
                break;
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.wrong_uri_) + uri);
        }
        if (database == null) database = new DB(getContext());
        int cnt = database.getDataBase().delete(DB.getTableContact(), selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public String getType(Uri uri) {
        String message = "MyContentProvider getType(): " + uri.toString();
        Messager.sendMessageToAllRecipients(getContext(), message);

        switch (uriMatcher.match(uri)) {

            case URI_CONTACTS :
                return CONTACT_CONTENT_TYPE;
            case URI_CONTACTS_ID :
                return CONTACT_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String message = "MyContentProvider insert(): " + uri.toString();
        Messager.sendMessageToAllRecipients(getContext(), message);
        if (uriMatcher.match(uri) != URI_CONTACTS) {
            throw new IllegalArgumentException(getContext().getString(R.string.wrong_uri_) + uri);
        }
        if (database == null) database = new DB(getContext());
        long rowID = database.getDataBase().insert(DB.getTableContact(), null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowID);
        //notification
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public boolean onCreate() {
        String message = "MyContentProvider onCreate()";
        Messager.sendMessageToAllRecipients(getContext(), message);
        if (database == null) database = new DB(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String message = "MyContentProvider query(): " + uri.toString();
        Messager.sendMessageToAllRecipients(getContext(), message);
        //test uri
        switch (uriMatcher.match(uri)) {

            case URI_CONTACTS : // - general uri
                message = "-------------------------> URI_CONTACTS";
                Messager.sendMessageToAllRecipients(getContext(), message);
                //if sort is Empty
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = DB.getTableName() + " ASC";
                }
                break;
            case URI_CONTACTS_ID : // - uri with id
                String id = uri.getLastPathSegment();
                message = "-------------------------> URI_CONTACTS_ID" + id;
                Messager.sendMessageToAllRecipients(getContext(), message);
                //add id to selection
                if (TextUtils.isEmpty(selection)) {
                    selection = DB.getTableId() + " = " + id;
                } else {
                    selection += (" AND " + DB.getTableId() + " = " + id);
                }
                break;
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.wrong_uri_) + uri);
        }

        if (database == null) database = new DB(getContext());
        Cursor cursor = database.getDataBase().query(DB.getTableContact(), projection, selection,
                selectionArgs, null, null, sortOrder);
        //включение для курсора режима уведомления об изменениях данных в CONTACT_CONTENT_URI
        cursor.setNotificationUri(getContext().getContentResolver(), CONTACT_CONTENT_URI);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String message = "MyContentProvider update()";
        Messager.sendMessageToAllRecipients(getContext(), message);

        switch (uriMatcher.match(uri)) {

            case URI_CONTACTS :
                message = "-------------------------> URI_CONTACTS";
                Messager.sendMessageToAllRecipients(getContext(), message);
                break;
            case URI_CONTACTS_ID : // - uri with id
                String id = uri.getLastPathSegment();
                message = "-------------------------> URI_CONTACTS_ID" + id;
                Messager.sendMessageToAllRecipients(getContext(), message);
                //add id to selection
                if (TextUtils.isEmpty(selection)) {
                    selection = DB.getTableId() + " = " + id;
                } else {
                    selection += (" AND " + DB.getTableId() + " = " + id);
                }
                break;
            default:
                throw new IllegalArgumentException(getContext().getString(R.string.wrong_uri_) + uri);
        }
        if (database == null) database = new DB(getContext());
        int cnt = database.getDataBase().update(DB.getTableContact(), values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
