package com.netcommlabs.greencontroller.sqlite_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netcommlabs.greencontroller.model.DataTransferModel;
import com.netcommlabs.greencontroller.model.ModalAddressModule;
import com.netcommlabs.greencontroller.model.MdlValveNameStateNdSelect;
import com.netcommlabs.greencontroller.model.ModalDeviceModule;
import com.netcommlabs.greencontroller.model.ModalDvcMD;
import com.netcommlabs.greencontroller.model.ModalValveMaster;
import com.netcommlabs.greencontroller.model.ModalValveSessionData;
import com.netcommlabs.greencontroller.utilities.MySharedPreference;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Android on 7/26/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    //private static Context mContext;
    private static DatabaseHandler databaseHandler;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "db_green_controller";

    //TABLE FOR ADDRESS MASTER
    private static final String TABLE_ADDRESS_MASTER = "table_address_master";
    private static final String CLM_ADDRESS_UUID = "address_uuid";
    private static final String CLM_RADIO_ADDRESS_NAME = "radio_addrs_name";
    private static final String CLM_ADDRESS_FLAT_HOUSE_BUILDING = "flat_house_building";
    private static final String CLM_ADDRESS_TOWER_STREET = "tower_street";
    private static final String CLM_ADDRESS_AREA_LAND_LOCALITY = "area_land_loca";
    private static final String CLM_ADDRESS_PIN_CODE = "pin_code";
    private static final String CLM_ADDRESS_CITY = "city";
    private static final String CLM_ADDRESS_STATE = "state";
    private static final String CLM_ADDRESS_SELECT_STATUS = "select_status";
    private static final String CLM_ADDRESS_PLACE_LATITUDE = "place_lat";
    private static final String CLM_ADDRESS_PLACE_LONGITUDE = "place_longi";
    private static final String CLM_ADDRESS_PLACE_WELL_KNOWN_NAME = "place_well_known_name";
    private static final String CLM_ADDRESS_PLACE_ADDRESS = "place_Address";
    private static final String CLM_ADDRESS_DELETE_DT = "delete_dt";
    private static final String CLM_ADDRESS_IS_SHOW_STATUS = "address_is_show_status";
    private static final String CLM_ADDRESS_CREATED_AT = "addrs_crtd_at";
    private static final String CLM_ADDRESS_UPDATED_AT = "addrs_updtd_at";


    //TABLE FOR DEVICE MASTER
    private static final String TABLE_DVC_MASTER = "table_dvc_master";
    private static final String CLM_DVC_UUID = "dvc_uuid";
    private static final String CLM_DVC_NAME = "dvc_name";
    private static final String CLM_DVC_MAC = "dvc_mac";
    private static final String CLM_DVC_TYPE = "dvc_type";
    private static final String CLM_DVC_QR_CODE = "dvc_qr_code";
    private static final String CLM_DVC_VALVE_NUM = "dvc_valve_num";
    private static final String CLM_DVC_LAST_CONNECTED = "dvc_last_connected";
    private static final String CLM_DVC_IS_SHOW_STATUS = "dvc_is_show_status";
    //APRD- Active, Pause, Resume and Delete
    private static final String CLM_DVC_OP_TP_APRD_STRING = "dvc_op_type_aprd_string";
    private static final String CLM_DVC_OP_TP_CON_DIS_STRING = "dvc_op_tp_con_discon";
    //Operation Type, 1-Add, 2-Edit, 3-Delete
    private static final String CLM_DVC_OP_TP_INT = "dvc_op_tp_aed_int";
    private static final String CLM_DVC_CREATED_DT = "dvc_created_at";
    private static final String CLM_DVC_UPDATED_DT = "dvc_updated_at";

    //TABLE FOR DEVICE LOG
    private static final String TABLE_DVC_LOG = "table_dvc_log";


    //TABLE FOR VALVE MASTER
    private static final String TABLE_VALVE_MASTER = "table_valve_master";
    private static final String CLM_VALVE_UUID = "valve_uuid";
    private static final String CLM_VALVE_NAME = "valve_name";
    //1- Select, 0-Deselect
    private static final String CLM_VALVE_SELECT_STATUS = "valve_select";
    //SPP- Stop, Play, and Pause
    private static final String CLM_VALVE_OP_TP_SPP_STRING = "valve_op_type_spp_string";
    private static final String CLM_VALVE_OP_TP_FLASH_ON_OF_STRING = "valve_op_type_flash_on_off_string";
    //Operation Type, 1-Add, 2-Edit
    private static final String CLM_VALVE_OP_TP_INT = "valve_operation_type_int";
    private static final String CLM_VALVE_CREATED_DT = "valve_created_at";
    private static final String CLM_VALVE_UPDATED_DT = "valve_updated_at";

    //TABLE FOR VALVE LOG
    private static final String TABLE_VALVE_LOG = "table_valve_log";


    //CLM TOTAL=13, TABLE FOR VALVE SESSION PLAN TEMP
    private static final String TABLE_VALVE_SESN_TEMP = "table_valve_sesn_temp";

    //CLM TOTAL=13, TABLE FOR VALVE SESSION MASTER
    private static final String TABLE_VALVE_SESN_MASTER = "table_valve_sesn_master";

    private static final String TABLE_VALVE_SESN_LOG = "table_valve_sesn_log";

    private static final String CLM_VALVE_SESN_NAME = "valve_name_sesn";
    private static final String CLM_VALVE_SESN_DISPOI = "valve_sesn_DP";
    private static final String CLM_VALVE_SESN_DURATION = "valve_sesn_duration";
    private static final String CLM_VALVE_SESN_QUANT = "valve_sesn_quant";
    private static final String CLM_VALVE_SESN_SLOT_NUM = "valve_sesn_slot_num";
    private static final String CLM_VALVE_SESN_DAY_NUM_SUN_TP = "sun_tp";
    private static final String CLM_VALVE_SESN_DAY_NUM_MON_TP = "mon_tp";
    private static final String CLM_VALVE_SESN_DAY_NUM_TUE_TP = "tue_tp";
    private static final String CLM_VALVE_SESN_DAY_NUM_WED_TP = "wed_tp";
    private static final String CLM_VALVE_SESN_DAY_NUM_THU_TP = "thu_tp";
    private static final String CLM_VALVE_SESN_DAY_NUM_FRI_TP = "fri_tp";
    private static final String CLM_VALVE_SESN_DAY_NUM_SAT_TP = "sat_tp";
    private static final String CLM_VALVE_SESN_OP_TP_INT = "valve_sesn_op_type_int";
    private static final String CLM_VALVE_SESN_CREATED_DT = "valve_sesn_crted_dt";


    private ModalAddressModule modalAddressModule;
    private ModalDeviceModule modalDeviceModule;
    private Context mContext;
    public String insertedDvcUUID = "";

    public static DatabaseHandler getInstance(Context context) {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler(context);
            databaseHandler.mContext = context;
        }
        return databaseHandler;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_ADDRESS_MASTER = "CREATE TABLE " + TABLE_ADDRESS_MASTER + " (" + CLM_ADDRESS_UUID + " TEXT," + CLM_RADIO_ADDRESS_NAME + " TEXT," + CLM_ADDRESS_FLAT_HOUSE_BUILDING + " TEXT," + CLM_ADDRESS_TOWER_STREET + " TEXT," + CLM_ADDRESS_AREA_LAND_LOCALITY + " TEXT," + CLM_ADDRESS_PIN_CODE + " TEXT," + CLM_ADDRESS_CITY + " TEXT," + CLM_ADDRESS_STATE + " TEXT," + CLM_ADDRESS_IS_SHOW_STATUS + " INTEGER," + CLM_ADDRESS_SELECT_STATUS + " INTEGER," + CLM_ADDRESS_PLACE_LATITUDE + " REAL," + CLM_ADDRESS_PLACE_LONGITUDE + " REAL," + CLM_ADDRESS_PLACE_WELL_KNOWN_NAME + " TEXT," + CLM_ADDRESS_PLACE_ADDRESS + " TEXT," /*+ CLM_ADDRESS_DELETE_STATUS + " INTEGER,"*/ + CLM_ADDRESS_DELETE_DT + " TEXT," + CLM_ADDRESS_CREATED_AT + " TEXT," + CLM_ADDRESS_UPDATED_AT + " TEXT )";

        String CREATE_TABLE_DEVICE_MASTER = "CREATE TABLE " + TABLE_DVC_MASTER + " (" + CLM_ADDRESS_UUID + " INTEGER," + CLM_DVC_UUID + " TEXT," + CLM_DVC_NAME + " TEXT," + CLM_DVC_MAC + " TEXT," + CLM_DVC_VALVE_NUM + " INTEGER," + CLM_DVC_TYPE + " TEXT," + CLM_DVC_QR_CODE + " TEXT," + CLM_DVC_OP_TP_APRD_STRING + " TEXT," + CLM_DVC_OP_TP_CON_DIS_STRING + " TEXT," + CLM_DVC_LAST_CONNECTED + " TEXT," + CLM_DVC_IS_SHOW_STATUS + " INTEGER," + CLM_DVC_OP_TP_INT + " INTEGER," + CLM_DVC_CREATED_DT + " TEXT," + CLM_DVC_UPDATED_DT + " TEXT )";
        String CREATE_TABLE_DEVICE_LOG = "CREATE TABLE " + TABLE_DVC_LOG + " (" + CLM_ADDRESS_UUID + " INTEGER," + CLM_DVC_UUID + " TEXT," + CLM_DVC_NAME + " TEXT," + CLM_DVC_MAC + " TEXT," + CLM_DVC_VALVE_NUM + " INTEGER," + CLM_DVC_TYPE + " TEXT," + CLM_DVC_QR_CODE + " TEXT," + CLM_DVC_OP_TP_APRD_STRING + " TEXT," + CLM_DVC_OP_TP_CON_DIS_STRING + " TEXT," + CLM_DVC_LAST_CONNECTED + " TEXT," + CLM_DVC_IS_SHOW_STATUS + " INTEGER," + CLM_DVC_OP_TP_INT + " INTEGER," + CLM_DVC_CREATED_DT + " TEXT )";

        String CREATE_TABLE_VALVE_MASTER = "CREATE TABLE " + TABLE_VALVE_MASTER + " (" + CLM_DVC_UUID + " TEXT," + CLM_VALVE_UUID + " TEXT ," + CLM_VALVE_NAME + " TEXT," + CLM_VALVE_SELECT_STATUS + " INTEGER," + CLM_VALVE_OP_TP_SPP_STRING + " TEXT," + CLM_VALVE_OP_TP_FLASH_ON_OF_STRING + " TEXT," + CLM_VALVE_OP_TP_INT + " INTEGER," + CLM_VALVE_CREATED_DT + " TEXT," + CLM_VALVE_UPDATED_DT + " TEXT )";
        String CREATE_TABLE_VALVE_LOG = "CREATE TABLE " + TABLE_VALVE_LOG + " (" + CLM_DVC_UUID + " TEXT," + CLM_VALVE_UUID + " TEXT," + CLM_VALVE_NAME + " TEXT," + CLM_VALVE_SELECT_STATUS + " INTEGER," + CLM_VALVE_OP_TP_SPP_STRING + " TEXT," + CLM_VALVE_OP_TP_FLASH_ON_OF_STRING + " TEXT," + CLM_VALVE_OP_TP_INT + " INTEGER," + CLM_VALVE_CREATED_DT + " TEXT )";

        String CREATE_TABLE_VALVE_SESN_PLN_TEMP = "CREATE TABLE " + TABLE_VALVE_SESN_TEMP + " (" + CLM_VALVE_UUID + " TEXT," + CLM_VALVE_SESN_NAME + " TEXT," + CLM_VALVE_SESN_DISPOI + " INTEGER," + CLM_VALVE_SESN_DURATION + " INTEGER," + CLM_VALVE_SESN_QUANT + " INTEGER," + CLM_VALVE_SESN_SLOT_NUM + " INTEGER," + CLM_VALVE_SESN_DAY_NUM_SUN_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_MON_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_TUE_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_WED_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_THU_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_FRI_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_SAT_TP + " TEXT," + CLM_VALVE_SESN_OP_TP_INT + " INTEGER)";
        String CREATE_TABLE_VALVE_SESN_MASTER = "CREATE TABLE " + TABLE_VALVE_SESN_MASTER + " (" + CLM_VALVE_UUID + " TEXT," + CLM_VALVE_SESN_NAME + " TEXT," + CLM_VALVE_SESN_DISPOI + " INTEGER," + CLM_VALVE_SESN_DURATION + " INTEGER," + CLM_VALVE_SESN_QUANT + " INTEGER," + CLM_VALVE_SESN_SLOT_NUM + " INTEGER," + CLM_VALVE_SESN_DAY_NUM_SUN_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_MON_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_TUE_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_WED_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_THU_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_FRI_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_SAT_TP + " TEXT," + CLM_VALVE_SESN_OP_TP_INT + " INTEGER," + CLM_VALVE_SESN_CREATED_DT + " TEXT )";
        String CREATE_TABLE_VALVE_SESN_LOG = "CREATE TABLE " + TABLE_VALVE_SESN_LOG + " (" + CLM_VALVE_UUID + " TEXT," + CLM_VALVE_SESN_NAME + " TEXT," + CLM_VALVE_SESN_DISPOI + " INTEGER," + CLM_VALVE_SESN_DURATION + " INTEGER," + CLM_VALVE_SESN_QUANT + " INTEGER," + CLM_VALVE_SESN_SLOT_NUM + " INTEGER," + CLM_VALVE_SESN_DAY_NUM_SUN_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_MON_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_TUE_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_WED_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_THU_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_FRI_TP + " TEXT," + CLM_VALVE_SESN_DAY_NUM_SAT_TP + " TEXT," + CLM_VALVE_SESN_OP_TP_INT + " INTEGER," + CLM_VALVE_SESN_CREATED_DT + " TEXT )";


        db.execSQL(CREATE_TABLE_ADDRESS_MASTER);

        db.execSQL(CREATE_TABLE_DEVICE_MASTER);
        db.execSQL(CREATE_TABLE_DEVICE_LOG);

        db.execSQL(CREATE_TABLE_VALVE_MASTER);
        db.execSQL(CREATE_TABLE_VALVE_LOG);

        db.execSQL(CREATE_TABLE_VALVE_SESN_PLN_TEMP);
        db.execSQL(CREATE_TABLE_VALVE_SESN_MASTER);
        db.execSQL(CREATE_TABLE_VALVE_SESN_LOG);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DVC_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DVC_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALVE_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALVE_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALVE_SESN_TEMP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALVE_SESN_MASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VALVE_SESN_LOG);

        // Create tables again
        onCreate(db);
    }

    public long insertAddressModule(String addressIDServer, ModalAddressModule modalAddressModule) {
        //addressUUID empty string means update for all address
        updateDeviceSelectStatus("", 0);

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_ADDRESS_UUID, addressIDServer);
        values.put(CLM_ADDRESS_FLAT_HOUSE_BUILDING, modalAddressModule.getFlat_num());
        values.put(CLM_ADDRESS_TOWER_STREET, modalAddressModule.getStreetName());
        values.put(CLM_ADDRESS_AREA_LAND_LOCALITY, modalAddressModule.getLocality_landmark());
        values.put(CLM_ADDRESS_PIN_CODE, modalAddressModule.getPinCode());
        values.put(CLM_ADDRESS_CITY, modalAddressModule.getCity());
        values.put(CLM_ADDRESS_STATE, modalAddressModule.getState());
        values.put(CLM_RADIO_ADDRESS_NAME, modalAddressModule.getAddressRadioName());
        values.put(CLM_ADDRESS_SELECT_STATUS, 1);
        values.put(CLM_ADDRESS_PLACE_LATITUDE, modalAddressModule.getLatitudeLocation());
        values.put(CLM_ADDRESS_PLACE_LONGITUDE, modalAddressModule.getLongitudeLocation());
        values.put(CLM_ADDRESS_PLACE_WELL_KNOWN_NAME, modalAddressModule.getPlaceWellKnownName());
        values.put(CLM_ADDRESS_PLACE_ADDRESS, modalAddressModule.getPlaceAddress());
        values.put(CLM_ADDRESS_IS_SHOW_STATUS, 1);
        values.putNull(CLM_ADDRESS_DELETE_DT);
        values.put(CLM_ADDRESS_CREATED_AT, getDateTime());
        values.putNull(CLM_ADDRESS_UPDATED_AT);

        long insertedRowUniqueID = db.insert(TABLE_ADDRESS_MASTER, null, values);
        //db.close();
        return insertedRowUniqueID;
    }

    public void updateAddressModule(ModalAddressModule modalAddressModule) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_ADDRESS_FLAT_HOUSE_BUILDING, modalAddressModule.getFlat_num());
        values.put(CLM_ADDRESS_TOWER_STREET, modalAddressModule.getStreetName());
        values.put(CLM_ADDRESS_AREA_LAND_LOCALITY, modalAddressModule.getLocality_landmark());
        values.put(CLM_ADDRESS_PIN_CODE, modalAddressModule.getPinCode());
        values.put(CLM_ADDRESS_CITY, modalAddressModule.getCity());
        values.put(CLM_ADDRESS_STATE, modalAddressModule.getState());
        values.put(CLM_RADIO_ADDRESS_NAME, modalAddressModule.getAddressRadioName());
        values.put(CLM_ADDRESS_PLACE_LATITUDE, modalAddressModule.getLatitudeLocation());
        values.put(CLM_ADDRESS_PLACE_LONGITUDE, modalAddressModule.getLongitudeLocation());
        values.put(CLM_ADDRESS_PLACE_WELL_KNOWN_NAME, modalAddressModule.getPlaceWellKnownName());
        values.put(CLM_ADDRESS_PLACE_ADDRESS, modalAddressModule.getPlaceAddress());
        values.put(CLM_ADDRESS_UPDATED_AT, getDateTime());

        long updatedRowUniqueID = db.update(TABLE_ADDRESS_MASTER, values, CLM_ADDRESS_UUID + " = ? ",
                new String[]{modalAddressModule.getAddressUUID()});
        //db.close();
    }

    public String getAddressUUID() {
        db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.query(TABLE_ADDRESS_MASTER, new String[]{CLM_ADDRESS_UUID}, CLM_ADDRESS_IS_SHOW_STATUS + " = ? ",
                new String[]{String.valueOf(1)}, null, null, null, null);
        cursor.moveToFirst();
        String addressUUID = cursor.getString(0);
        cursor.close();
        //db.close();
        return addressUUID;
    }

    public long insertDeviceModule(String addressUUID, String dvcNameEdited, String dvc_mac_address, String qrCodeEdited, int valveNum) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_ADDRESS_UUID, addressUUID);
        insertedDvcUUID = generateUUID();
        values.put(CLM_DVC_UUID, insertedDvcUUID);
        values.put(CLM_DVC_NAME, dvcNameEdited);
        values.put(CLM_DVC_MAC, dvc_mac_address);
        values.put(CLM_DVC_QR_CODE, qrCodeEdited);
        values.put(CLM_DVC_VALVE_NUM, valveNum);
        values.putNull(CLM_DVC_TYPE);
        values.put(CLM_DVC_LAST_CONNECTED, MySharedPreference.getInstance(mContext).getLastConnectedTime());
        values.put(CLM_DVC_OP_TP_APRD_STRING, "ACTIVE");
        values.put(CLM_DVC_IS_SHOW_STATUS, 1);
        values.put(CLM_DVC_OP_TP_CON_DIS_STRING, "Connected");
        values.put(CLM_DVC_OP_TP_INT, 1);
        values.put(CLM_DVC_CREATED_DT, getDateTime());
        values.putNull(CLM_DVC_UPDATED_DT);

        long insertedRowID = db.insert(TABLE_DVC_MASTER, null, values);
        //db.close();

        return insertedRowID;
    }

    public void insertDeviceModuleLog(String insertedDvcUUID) {
        db = this.getWritableDatabase();
        Cursor cursor;
        ContentValues values = new ContentValues();

        cursor = db.query(TABLE_DVC_MASTER, null, CLM_DVC_UUID + "= ? AND " + CLM_DVC_IS_SHOW_STATUS + " = ? ", new String[]{insertedDvcUUID, String.valueOf(1)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            values.put(CLM_ADDRESS_UUID, cursor.getString(0));
            values.put(CLM_DVC_UUID, cursor.getString(1));
            values.put(CLM_DVC_NAME, cursor.getString(2));
            values.put(CLM_DVC_MAC, cursor.getString(3));
            values.put(CLM_DVC_VALVE_NUM, cursor.getInt(4));
            values.put(CLM_DVC_TYPE, cursor.getString(5));
            values.put(CLM_DVC_QR_CODE, cursor.getString(6));
            values.put(CLM_DVC_OP_TP_APRD_STRING, cursor.getString(7));
            values.put(CLM_DVC_OP_TP_CON_DIS_STRING, cursor.getString(8));
            values.put(CLM_DVC_LAST_CONNECTED, cursor.getString(9));
            values.put(CLM_DVC_IS_SHOW_STATUS, cursor.getInt(10));
            values.put(CLM_DVC_OP_TP_INT, cursor.getInt(11));
            values.put(CLM_DVC_CREATED_DT, cursor.getString(12));
        }

        db.insert(TABLE_DVC_LOG, null, values);
        //db.close();
    }

    public void insertValveMaster(String dvcUUID, ModalValveMaster modalValveMaster) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_DVC_UUID, dvcUUID);
        values.put(CLM_VALVE_UUID, generateUUID());
        values.put(CLM_VALVE_NAME, modalValveMaster.getValveName());
        values.put(CLM_VALVE_SELECT_STATUS, modalValveMaster.getValveSelectStatus());
        values.put(CLM_VALVE_OP_TP_SPP_STRING, modalValveMaster.getValveOpTpSPP());
        values.put(CLM_VALVE_OP_TP_FLASH_ON_OF_STRING, modalValveMaster.getValveOpTpFlushONOFF());
        values.put(CLM_VALVE_OP_TP_INT, modalValveMaster.getValveOpTPInt());
        values.put(CLM_VALVE_CREATED_DT, getDateTime());
        values.putNull(CLM_VALVE_UPDATED_DT);

        long insertedRowUniqueID = db.insert(TABLE_VALVE_MASTER, null, values);
        //db.close();
    }

    public void insertValveMasterLog(String dvcUUID) {
        db = this.getWritableDatabase();
        Cursor cursor;
        ContentValues values = new ContentValues();

        cursor = db.query(TABLE_VALVE_MASTER, null, CLM_DVC_UUID + " = ?", new String[]{dvcUUID}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                values.put(CLM_DVC_UUID, cursor.getString(0));
                values.put(CLM_VALVE_UUID, cursor.getString(1));
                values.put(CLM_VALVE_NAME, cursor.getString(2));
                values.put(CLM_VALVE_SELECT_STATUS, cursor.getInt(3));
                values.put(CLM_VALVE_OP_TP_SPP_STRING, cursor.getString(4));
                values.put(CLM_VALVE_OP_TP_FLASH_ON_OF_STRING, cursor.getString(5));
                values.put(CLM_VALVE_OP_TP_INT, cursor.getInt(6));
                values.put(CLM_VALVE_CREATED_DT, cursor.getString(7));

                db.insert(TABLE_VALVE_LOG, null, values);
            } while (cursor.moveToNext());
        }

        //db.close();
    }

    public int updateSesnTimePointsTemp(int dayInt, int timePoint, int timeSlotNum) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String timePointString;
        int numOfRowsAffected = 0;
        //Updating all rows with empty string first
        if (dayInt == 0 && timePoint == 0 && timeSlotNum == 0)
        {
            values.put(CLM_VALVE_SESN_DAY_NUM_SUN_TP, "");
            values.put(CLM_VALVE_SESN_DAY_NUM_MON_TP, "");
            values.put(CLM_VALVE_SESN_DAY_NUM_TUE_TP, "");
            values.put(CLM_VALVE_SESN_DAY_NUM_WED_TP, "");
            values.put(CLM_VALVE_SESN_DAY_NUM_THU_TP, "");
            values.put(CLM_VALVE_SESN_DAY_NUM_FRI_TP, "");
            values.put(CLM_VALVE_SESN_DAY_NUM_SAT_TP, "");

            numOfRowsAffected = db.update(TABLE_VALVE_SESN_TEMP, values, null, null);
            Log.e("", "");
        } else {
            if (timePoint < 10) {
                timePointString = "0" + timePoint + ":00";
            } else {
                timePointString = timePoint + ":00";
            }

            switch (dayInt) {
                case 1:
                    values.put(CLM_VALVE_SESN_DAY_NUM_SUN_TP, timePointString);
                    break;
                case 2:
                    values.put(CLM_VALVE_SESN_DAY_NUM_MON_TP, timePointString);
                    break;
                case 3:
                    values.put(CLM_VALVE_SESN_DAY_NUM_TUE_TP, timePointString);
                    break;
                case 4:
                    values.put(CLM_VALVE_SESN_DAY_NUM_WED_TP, timePointString);
                    break;
                case 5:
                    values.put(CLM_VALVE_SESN_DAY_NUM_THU_TP, timePointString);
                    break;
                case 6:
                    values.put(CLM_VALVE_SESN_DAY_NUM_FRI_TP, timePointString);
                    break;
                case 7:
                    values.put(CLM_VALVE_SESN_DAY_NUM_SAT_TP, timePointString);
            }
            numOfRowsAffected = db.update(TABLE_VALVE_SESN_TEMP, values, CLM_VALVE_SESN_SLOT_NUM + " = ? ",
                    new String[]{String.valueOf(timeSlotNum)});
        }
        //db.close();
        return numOfRowsAffected;
    }

    public void updateValveDPDurationQuantTemp(int etDisPntsInt, int etDurationInt, int etWaterQuantInt, String clkdVlvUUID) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_VALVE_SESN_DISPOI, etDisPntsInt);
        values.put(CLM_VALVE_SESN_DURATION, etDurationInt);
        values.put(CLM_VALVE_SESN_QUANT, etWaterQuantInt);

        db.update(TABLE_VALVE_SESN_TEMP, values, null, null);
        //db.close();
    }

    public List<ModalValveMaster> getValveMaster(String dvcUUID) {
        db = this.getReadableDatabase();
        Cursor cursor;
        List<ModalValveMaster> listModalValveMasters = new ArrayList();
        cursor = db.query(TABLE_VALVE_MASTER, new String[]{CLM_DVC_UUID, CLM_VALVE_UUID, CLM_VALVE_NAME, CLM_VALVE_SELECT_STATUS, CLM_VALVE_OP_TP_SPP_STRING, CLM_VALVE_OP_TP_FLASH_ON_OF_STRING}, CLM_DVC_UUID + " = ? ",
                new String[]{dvcUUID}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ModalValveMaster modalValveMaster = new ModalValveMaster(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5));
                listModalValveMasters.add(modalValveMaster);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return listModalValveMasters;
    }

    public List<String> getAllDeviceName() {
        db = this.getReadableDatabase();
        ArrayList<String> listDeviceUniqueName = new ArrayList<>();
        Cursor cursor = db.query(TABLE_DVC_MASTER, new String[]{CLM_DVC_NAME}, CLM_DVC_IS_SHOW_STATUS + " = ? ", new String[]{String.valueOf(1)}, null, null, null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listDeviceUniqueName.add(cursor.getString(cursor.getColumnIndex(CLM_DVC_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        // return all device name
        return listDeviceUniqueName;
    }

    public List<ModalAddressModule> getAlladdressUUIDRadioNameSelectStatus() {
        db = this.getReadableDatabase();
        ArrayList<ModalAddressModule> listAddressIDRadioNameSelectStatus = new ArrayList<>();

        Cursor cursor = db.query(TABLE_ADDRESS_MASTER, new String[]{CLM_ADDRESS_UUID, CLM_RADIO_ADDRESS_NAME, CLM_ADDRESS_SELECT_STATUS}, CLM_ADDRESS_IS_SHOW_STATUS + " = ? ", new String[]{String.valueOf(1)}, null, null, null);
        // looping through all cursor rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ModalAddressModule modalAddressModule = new ModalAddressModule(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
                listAddressIDRadioNameSelectStatus.add(modalAddressModule);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return listAddressIDRadioNameSelectStatus;
    }

    public List<ModalAddressModule> getAddressWithLocation(String addressUUID) {
        db = this.getReadableDatabase();
        List<ModalAddressModule> listModalAddressModule = new ArrayList<>();
        Cursor cursor;

        //Getting all addresses with location
        if (addressUUID.isEmpty()) {
            //Get all Address with location
            cursor = db.query(TABLE_ADDRESS_MASTER, new String[]{CLM_ADDRESS_UUID, CLM_ADDRESS_FLAT_HOUSE_BUILDING, CLM_ADDRESS_TOWER_STREET, CLM_ADDRESS_AREA_LAND_LOCALITY, CLM_ADDRESS_PIN_CODE, CLM_ADDRESS_CITY, CLM_ADDRESS_STATE, CLM_RADIO_ADDRESS_NAME, CLM_ADDRESS_PLACE_LATITUDE, CLM_ADDRESS_PLACE_LONGITUDE, CLM_ADDRESS_PLACE_WELL_KNOWN_NAME, CLM_ADDRESS_PLACE_ADDRESS}, CLM_ADDRESS_IS_SHOW_STATUS + " = ? ",
                    new String[]{String.valueOf(1)}, null, null, null, null);

        } else {
            //Getting single address with location using addressUUID
            cursor = db.query(TABLE_ADDRESS_MASTER, new String[]{CLM_ADDRESS_UUID, CLM_ADDRESS_FLAT_HOUSE_BUILDING, CLM_ADDRESS_TOWER_STREET, CLM_ADDRESS_AREA_LAND_LOCALITY, CLM_ADDRESS_PIN_CODE, CLM_ADDRESS_CITY, CLM_ADDRESS_STATE, CLM_RADIO_ADDRESS_NAME, CLM_ADDRESS_PLACE_LATITUDE, CLM_ADDRESS_PLACE_LONGITUDE, CLM_ADDRESS_PLACE_WELL_KNOWN_NAME, CLM_ADDRESS_PLACE_ADDRESS}, CLM_ADDRESS_UUID + " = ? AND " + CLM_ADDRESS_IS_SHOW_STATUS + " = ? ",
                    new String[]{addressUUID, String.valueOf(1)}, null, null, null, null);

        }
        if (cursor != null && cursor.moveToFirst()) {
            do {
                modalAddressModule = new ModalAddressModule(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getDouble(8), cursor.getDouble(9), cursor.getString(10), cursor.getString(11));
                listModalAddressModule.add(modalAddressModule);
            } while (cursor.moveToNext());

        }
        cursor.close();
        //db.close();
        return listModalAddressModule;
    }

    public List<ModalAddressModule> getAddressListFormData(String addressUUID) {
        db = this.getReadableDatabase();
        List<ModalAddressModule> listModalAddressModule = new ArrayList<>();
        Cursor cursor;
        //Get all Address form data
        if (addressUUID.isEmpty()) {
            cursor = db.query(TABLE_ADDRESS_MASTER, new String[]{CLM_ADDRESS_UUID, CLM_ADDRESS_FLAT_HOUSE_BUILDING, CLM_ADDRESS_TOWER_STREET, CLM_ADDRESS_AREA_LAND_LOCALITY, CLM_ADDRESS_PIN_CODE, CLM_ADDRESS_CITY, CLM_ADDRESS_STATE, CLM_RADIO_ADDRESS_NAME}, CLM_ADDRESS_IS_SHOW_STATUS + " = ? ",
                    new String[]{String.valueOf(1)}, null, null, null, null);
        } else {
            cursor = db.query(TABLE_ADDRESS_MASTER, new String[]{CLM_ADDRESS_UUID, CLM_ADDRESS_FLAT_HOUSE_BUILDING, CLM_ADDRESS_TOWER_STREET, CLM_ADDRESS_AREA_LAND_LOCALITY, CLM_ADDRESS_PIN_CODE, CLM_ADDRESS_CITY, CLM_ADDRESS_STATE, CLM_RADIO_ADDRESS_NAME}, CLM_ADDRESS_UUID + " = ? AND " + CLM_ADDRESS_IS_SHOW_STATUS + " = ? ",
                    new String[]{addressUUID, String.valueOf(1)}, null, null, null, null);
        }
        if (cursor != null && cursor.moveToFirst()) {
            do {
                modalAddressModule = new ModalAddressModule(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                listModalAddressModule.add(modalAddressModule);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return listModalAddressModule;
    }

    public List<ModalDeviceModule> getDeviceDataForIMap(String addressUUID) {
        db = this.getWritableDatabase();
        Cursor cursor;
        List<ModalDeviceModule> listModalDeviceModule = new ArrayList();
        if (addressUUID.equals("")) {
            cursor = db.query(TABLE_DVC_MASTER, new String[]{CLM_DVC_UUID, CLM_DVC_NAME, CLM_DVC_MAC, CLM_DVC_VALVE_NUM}, CLM_DVC_IS_SHOW_STATUS + " = ? ",
                    new String[]{String.valueOf(1)}, null, null, null, null);
        } else {
            cursor = db.query(TABLE_DVC_MASTER, new String[]{CLM_DVC_UUID, CLM_DVC_NAME, CLM_DVC_MAC, CLM_DVC_VALVE_NUM}, CLM_ADDRESS_UUID + " = ? AND " + CLM_DVC_IS_SHOW_STATUS + " = ? ",
                    new String[]{String.valueOf(addressUUID), String.valueOf(1)}, null, null, null, null);
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                modalDeviceModule = new ModalDeviceModule(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                listModalDeviceModule.add(modalDeviceModule);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return listModalDeviceModule;
    }

    public List<String> getAllDeviceMAC() {
        db = this.getReadableDatabase();
        List<String> listDeviceMAC = new ArrayList<>();

        Cursor cursor = db.query(TABLE_DVC_MASTER, new String[]{CLM_DVC_MAC}, CLM_DVC_IS_SHOW_STATUS + " = ? ",
                new String[]{String.valueOf(1)}, null, null, null, null);
        // looping through all cursor rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                listDeviceMAC.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        // return all device MAC
        return listDeviceMAC;
    }

    public ModalDeviceModule getDeviceNdValveNumAtAddress(String addressUUID) {
        db = this.getReadableDatabase();
        Cursor cursor;
        int deviceNum = 0, valveNum = 0;
        cursor = db.query(TABLE_DVC_MASTER, new String[]{CLM_DVC_UUID, CLM_DVC_VALVE_NUM}, CLM_ADDRESS_UUID + " = ? AND " + CLM_DVC_IS_SHOW_STATUS + " = ?",
                new String[]{addressUUID, String.valueOf(1)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                deviceNum = deviceNum + 1;
                valveNum = valveNum + cursor.getInt(1);
            } while (cursor.moveToNext());
        }
        modalDeviceModule = new ModalDeviceModule(deviceNum, valveNum);

        cursor.close();
        //db.close();
        return modalDeviceModule;
    }

    public void updateDeviceSelectStatus(String addressUUID, int deviceSelectStatus) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLM_ADDRESS_SELECT_STATUS, deviceSelectStatus);
        if (addressUUID.equals("")) {
            db.update(TABLE_ADDRESS_MASTER, values, null, null);
        } else {
            db.update(TABLE_ADDRESS_MASTER, values, CLM_ADDRESS_UUID + " = ? ",
                    new String[]{String.valueOf(addressUUID)});
        }
        //db.close();
    }

    // Updating valve selection
    public void updateValveSelectStatus(String clickedValveUUID, int valveSelectStatus) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_VALVE_SELECT_STATUS, valveSelectStatus);
        int rowAffected = db.update(TABLE_VALVE_MASTER, values, CLM_VALVE_UUID + " = ?",
                new String[]{clickedValveUUID});
        //db.close();
    }

    // Updating valve status, SPP- STOP, PLAY and PAUSE
    public int updateValveOpTpSPPStatus(String dvcUUID, String valveUUID, String valveOpTpStatus) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLM_VALVE_OP_TP_SPP_STRING, valveOpTpStatus);
        values.put(CLM_VALVE_OP_TP_INT, 2);
        values.put(CLM_VALVE_UPDATED_DT, getDateTime());
        int rowAffected = 0;
        // Will effect all valves of given device ID, from I Map
        if (!dvcUUID.isEmpty()) {
            if (valveOpTpStatus.equals("PAUSE")) {
                rowAffected = db.update(TABLE_VALVE_MASTER, values, CLM_DVC_UUID + " = ? AND " + CLM_VALVE_OP_TP_SPP_STRING + " = ? ",
                        new String[]{dvcUUID, "PLAY"});
            } else if (valveOpTpStatus.equals("PLAY")) {
                rowAffected = db.update(TABLE_VALVE_MASTER, values, CLM_DVC_UUID + " = ? AND " + CLM_VALVE_OP_TP_SPP_STRING + " = ? ",
                        new String[]{dvcUUID, "PAUSE"});
            } else if (valveOpTpStatus.equals("STOP")) {
                rowAffected = db.update(TABLE_VALVE_MASTER, values, CLM_DVC_UUID + " = ? AND " + CLM_VALVE_OP_TP_SPP_STRING + " = ? OR " + CLM_VALVE_OP_TP_SPP_STRING + " = ?",
                        new String[]{dvcUUID, "PLAY", "PAUSE"});
            }
        }
        // Will effect only single valve
        else if (!valveUUID.isEmpty()) {
            if (valveOpTpStatus.equals("STOP")) {
                values.put(CLM_VALVE_OP_TP_FLASH_ON_OF_STRING, "FLUSH OFF");
                values.put(CLM_VALVE_OP_TP_INT, 3);
            }
            rowAffected = db.update(TABLE_VALVE_MASTER, values, CLM_VALVE_UUID + " = ? ",
                    new String[]{valveUUID});
        }
        //db.close();
        return rowAffected;
    }

    // Updating Play Pause
    public int updateValveFlushStatus(String valveUUID, String flushStatus) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLM_VALVE_OP_TP_INT, 2);
        values.put(CLM_VALVE_OP_TP_FLASH_ON_OF_STRING, flushStatus);

        int rowAffected = db.update(TABLE_VALVE_MASTER, values, CLM_VALVE_UUID + " = ? ",
                new String[]{valveUUID});
        //db.close();
        return rowAffected;
    }

    public void deleteUpdateAddress(String addressUUID) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CLM_ADDRESS_IS_SHOW_STATUS, 0);
        cv.put(CLM_ADDRESS_UPDATED_AT, getDateTime());
        int deleteUpdateConfirm = db.update(TABLE_ADDRESS_MASTER, cv, CLM_ADDRESS_UUID + " = ?",
                new String[]{addressUUID});
        //db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public ArrayList<ModalValveSessionData> getValveSessionData(String clickedVlvUUID) {
        db = this.getReadableDatabase();
        ModalValveSessionData modalValveSessionData;
        ArrayList<ModalValveSessionData> listModalValveSessionData = new ArrayList<>();
        Cursor myCursor = db.query(TABLE_VALVE_SESN_MASTER, new String[]{CLM_VALVE_SESN_NAME, CLM_VALVE_SESN_DISPOI, CLM_VALVE_SESN_DURATION, CLM_VALVE_SESN_QUANT, CLM_VALVE_SESN_SLOT_NUM, CLM_VALVE_SESN_DAY_NUM_SUN_TP, CLM_VALVE_SESN_DAY_NUM_MON_TP, CLM_VALVE_SESN_DAY_NUM_TUE_TP, CLM_VALVE_SESN_DAY_NUM_WED_TP, CLM_VALVE_SESN_DAY_NUM_THU_TP, CLM_VALVE_SESN_DAY_NUM_FRI_TP, CLM_VALVE_SESN_DAY_NUM_SAT_TP}, CLM_VALVE_UUID + " = ?",
                new String[]{clickedVlvUUID}, null, null, null, null);

        if (myCursor != null && myCursor.moveToFirst()) {
            do {
                modalValveSessionData = new ModalValveSessionData(myCursor.getString(0), myCursor.getInt(1), myCursor.getInt(2), myCursor.getInt(3), myCursor.getInt(4), myCursor.getString(5), myCursor.getString(6), myCursor.getString(7), myCursor.getString(8), myCursor.getString(9), myCursor.getString(10), myCursor.getString(11));

                listModalValveSessionData.add(modalValveSessionData);
            } while (myCursor.moveToNext());
            myCursor.close();
        }
        //db.close();
        return listModalValveSessionData;
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public void dbOperationBWSesnTempMasterNdLog(String clickedVlvUUID, String valveNameSession) {
        db = this.getWritableDatabase();
        ModalValveSessionData mvsd;

        Cursor cursorTemp = db.query(TABLE_VALVE_SESN_TEMP, new String[]{CLM_VALVE_SESN_NAME, CLM_VALVE_SESN_DISPOI, CLM_VALVE_SESN_DURATION, CLM_VALVE_SESN_QUANT, CLM_VALVE_SESN_SLOT_NUM, CLM_VALVE_SESN_DAY_NUM_SUN_TP, CLM_VALVE_SESN_DAY_NUM_MON_TP, CLM_VALVE_SESN_DAY_NUM_TUE_TP, CLM_VALVE_SESN_DAY_NUM_WED_TP, CLM_VALVE_SESN_DAY_NUM_THU_TP, CLM_VALVE_SESN_DAY_NUM_FRI_TP, CLM_VALVE_SESN_DAY_NUM_SAT_TP}, null, null, null, null, null);
        if (cursorTemp != null && cursorTemp.moveToFirst()) {
            do {
                mvsd = new ModalValveSessionData(cursorTemp.getString(0), cursorTemp.getInt(1), cursorTemp.getInt(2), cursorTemp.getInt(3), cursorTemp.getInt(4), cursorTemp.getString(5), cursorTemp.getString(6), cursorTemp.getString(7), cursorTemp.getString(8), cursorTemp.getString(9), cursorTemp.getString(10), cursorTemp.getString(11));
                Cursor cursorMaster = db.query(TABLE_VALVE_SESN_MASTER, null, CLM_VALVE_UUID + " = ? AND " + CLM_VALVE_SESN_NAME + " = ? AND " + CLM_VALVE_SESN_DISPOI + " = ? AND " + CLM_VALVE_SESN_DURATION + " = ? AND " + CLM_VALVE_SESN_QUANT + " = ? AND " + CLM_VALVE_SESN_SLOT_NUM + " = ? AND " + CLM_VALVE_SESN_DAY_NUM_SUN_TP + " = ? AND " + CLM_VALVE_SESN_DAY_NUM_MON_TP + " = ? AND " + CLM_VALVE_SESN_DAY_NUM_TUE_TP + " = ? AND " + CLM_VALVE_SESN_DAY_NUM_WED_TP + " = ? AND " + CLM_VALVE_SESN_DAY_NUM_THU_TP + " = ? AND " + CLM_VALVE_SESN_DAY_NUM_FRI_TP + " = ? AND " + CLM_VALVE_SESN_DAY_NUM_SAT_TP + " = ? ",
                        new String[]{clickedVlvUUID, mvsd.getValveNameSession(), String.valueOf(mvsd.getSessionDP()), String.valueOf(mvsd.getSessionDuration()), String.valueOf(mvsd.getSessionQuantity()), String.valueOf(mvsd.getSesnSlotNum()), mvsd.getSunTP(), mvsd.getMonTP(), mvsd.getTueTP(), mvsd.getWedTP(), mvsd.getThuTP(), mvsd.getFriTP(), mvsd.getSatTP()}, null, null, null, null);

                if (cursorMaster.getCount() == 0) {
                    Log.e("GGG ROW NOT MATCHED ", "DUMMY");
                    insertValveSesnLog(clickedVlvUUID, valveNameSession, mvsd, db);
                    db.delete(TABLE_VALVE_SESN_MASTER, CLM_VALVE_UUID + " =? AND " + CLM_VALVE_SESN_SLOT_NUM + " = ? ", new String[]{clickedVlvUUID, String.valueOf(mvsd.getSesnSlotNum())});
                    insertValveSesnMaster(clickedVlvUUID, valveNameSession, mvsd, db);
                }
            } while (cursorTemp.moveToNext());
            cursorTemp.close();
        }
        //db.close();
    }

    public void insertValveSesnTemp(String valveUUID, String valveName, int slotNum) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_VALVE_UUID, valveUUID);
        values.put(CLM_VALVE_SESN_NAME, valveName);
        values.putNull(CLM_VALVE_SESN_DISPOI);
        values.putNull(CLM_VALVE_SESN_DURATION);
        values.putNull(CLM_VALVE_SESN_QUANT);
        values.put(CLM_VALVE_SESN_SLOT_NUM, slotNum);
        values.putNull(CLM_VALVE_SESN_DAY_NUM_SUN_TP);
        values.putNull(CLM_VALVE_SESN_DAY_NUM_MON_TP);
        values.putNull(CLM_VALVE_SESN_DAY_NUM_TUE_TP);
        values.putNull(CLM_VALVE_SESN_DAY_NUM_WED_TP);
        values.putNull(CLM_VALVE_SESN_DAY_NUM_THU_TP);
        values.putNull(CLM_VALVE_SESN_DAY_NUM_FRI_TP);
        values.putNull(CLM_VALVE_SESN_DAY_NUM_SAT_TP);
        values.put(CLM_VALVE_SESN_OP_TP_INT, 1);

        db.insert(TABLE_VALVE_SESN_TEMP, null, values);
        //db.close();
    }

    public void insertValveSesnMaster(String valveUUID, String valveName, ModalValveSessionData mvsd, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(CLM_VALVE_UUID, valveUUID);
        values.put(CLM_VALVE_SESN_NAME, valveName);
        values.put(CLM_VALVE_SESN_DISPOI, mvsd.getSessionDP());
        values.put(CLM_VALVE_SESN_DURATION, mvsd.getSessionDuration());
        values.put(CLM_VALVE_SESN_QUANT, mvsd.getSessionQuantity());
        values.put(CLM_VALVE_SESN_SLOT_NUM, mvsd.getSesnSlotNum());
        values.put(CLM_VALVE_SESN_DAY_NUM_SUN_TP, mvsd.getSunTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_MON_TP, mvsd.getMonTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_TUE_TP, mvsd.getTueTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_WED_TP, mvsd.getWedTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_THU_TP, mvsd.getThuTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_FRI_TP, mvsd.getFriTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_SAT_TP, mvsd.getSatTP());
        values.put(CLM_VALVE_SESN_OP_TP_INT, 1);
        values.put(CLM_VALVE_SESN_CREATED_DT, getDateTime());

        long insertedRowUniqueID = db.insert(TABLE_VALVE_SESN_MASTER, null, values);
    }

    private void insertValveSesnLog(String clickedVlvUUID, String valveName, ModalValveSessionData mvsd, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(CLM_VALVE_UUID, clickedVlvUUID);
        values.put(CLM_VALVE_SESN_NAME, valveName);
        values.put(CLM_VALVE_SESN_DISPOI, mvsd.getSessionDP());
        values.put(CLM_VALVE_SESN_DURATION, mvsd.getSessionDuration());
        values.put(CLM_VALVE_SESN_QUANT, mvsd.getSessionQuantity());
        values.put(CLM_VALVE_SESN_SLOT_NUM, mvsd.getSesnSlotNum());
        values.put(CLM_VALVE_SESN_DAY_NUM_SUN_TP, mvsd.getSunTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_MON_TP, mvsd.getMonTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_TUE_TP, mvsd.getTueTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_WED_TP, mvsd.getWedTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_THU_TP, mvsd.getThuTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_FRI_TP, mvsd.getFriTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_SAT_TP, mvsd.getSatTP());
        values.put(CLM_VALVE_SESN_OP_TP_INT, 1);
        values.put(CLM_VALVE_SESN_CREATED_DT, getDateTime());

        db.insert(TABLE_VALVE_SESN_LOG, null, values);
    }

    public int updateDvcNameOnly(String dvcUUID, String editedName) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CLM_DVC_NAME, editedName);
        values.put(CLM_DVC_OP_TP_INT, 2);

        int rowAffected = db.update(TABLE_DVC_MASTER, values, CLM_DVC_UUID + " = ? ",
                new String[]{dvcUUID});

        return rowAffected;
    }

    public int getDvcTotalValvesPlayPauseCount(String dvcUUID, String checkOpTySPP) {
        db = this.getReadableDatabase();
        int totalCounts = 0;
        Cursor cursor;

        if (checkOpTySPP.equals("STOP")) {
            cursor = db.query(TABLE_VALVE_MASTER, new String[]{CLM_VALVE_OP_TP_SPP_STRING}, CLM_DVC_UUID + " = ? AND " + CLM_VALVE_OP_TP_SPP_STRING + " = ? OR " + CLM_VALVE_OP_TP_SPP_STRING + " = ?",
                    new String[]{dvcUUID, "PLAY", "PAUSE"}, null, null, null, null);
        } else {
            cursor = db.query(TABLE_VALVE_MASTER, new String[]{CLM_VALVE_OP_TP_SPP_STRING}, CLM_DVC_UUID + " = ? AND " + CLM_VALVE_OP_TP_SPP_STRING + " = ? ",
                    new String[]{dvcUUID, checkOpTySPP}, null, null, null, null);
        }
        if (cursor != null) {
            totalCounts = cursor.getCount();
        }
        cursor.close();
        //db.close();
        return totalCounts;
    }

    public int deleteUpdateDevice(String dvcUUID) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_DVC_OP_TP_APRD_STRING, "DELETE");
        values.put(CLM_DVC_IS_SHOW_STATUS, 0);
        values.put(CLM_DVC_OP_TP_INT, 3);
        values.put(CLM_DVC_UPDATED_DT, getDateTime());

        int rowAffected = db.update(TABLE_DVC_MASTER, values, CLM_DVC_UUID + " = ? ",
                new String[]{dvcUUID});

        //db.close();
        return rowAffected;
    }

    public int deleteValveSessionData(String clickedVlvUUID) {
        db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_VALVE_SESN_MASTER, CLM_VALVE_UUID + " = ?",
                new String[]{clickedVlvUUID});
        //db.close();
        return deletedRows;
    }

    public void deleteValveSesnTEMP() {
        db = this.getWritableDatabase();
        db.delete(TABLE_VALVE_SESN_TEMP, null, null);
        //db.close();
    }

    public int updateDvcOpTyStringAll(String dvcUUID, String opTyString) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (opTyString.equals("Connected") || opTyString.equals("Disconnected")) {
            values.put(CLM_DVC_OP_TP_CON_DIS_STRING, opTyString);
            values.put(CLM_DVC_OP_TP_INT, 2);
            values.put(CLM_DVC_UPDATED_DT, getDateTime());
        } else {
            values.put(CLM_DVC_OP_TP_APRD_STRING, opTyString);
            values.put(CLM_DVC_OP_TP_INT, 2);
            values.put(CLM_DVC_UPDATED_DT, getDateTime());
        }

        int rowsEffected = db.update(TABLE_DVC_MASTER, values, CLM_DVC_UUID + " = ? ",
                new String[]{dvcUUID});

        return rowsEffected;
    }

    public void selectFrmVlvSesnMasterInsertIntoLog(String clickedVlvUUID) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Cursor cursor;
        cursor = db.query(TABLE_VALVE_SESN_MASTER, null, CLM_VALVE_UUID + " = ?", new String[]{clickedVlvUUID}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                cv.put(CLM_VALVE_UUID, cursor.getString(0));
                cv.put(CLM_VALVE_SESN_NAME, cursor.getString(1));
                cv.put(CLM_VALVE_SESN_DISPOI, cursor.getString(2));
                cv.put(CLM_VALVE_SESN_DURATION, cursor.getString(3));
                cv.put(CLM_VALVE_SESN_QUANT, cursor.getString(4));
                cv.put(CLM_VALVE_SESN_SLOT_NUM, cursor.getString(5));
                cv.put(CLM_VALVE_SESN_DAY_NUM_SUN_TP, cursor.getString(6));
                cv.put(CLM_VALVE_SESN_DAY_NUM_MON_TP, cursor.getString(7));
                cv.put(CLM_VALVE_SESN_DAY_NUM_TUE_TP, cursor.getString(8));
                cv.put(CLM_VALVE_SESN_DAY_NUM_WED_TP, cursor.getString(9));
                cv.put(CLM_VALVE_SESN_DAY_NUM_THU_TP, cursor.getString(10));
                cv.put(CLM_VALVE_SESN_DAY_NUM_FRI_TP, cursor.getString(11));
                cv.put(CLM_VALVE_SESN_DAY_NUM_SAT_TP, cursor.getString(12));
                cv.put(CLM_VALVE_SESN_OP_TP_INT, 3);
                cv.put(CLM_VALVE_SESN_CREATED_DT, cursor.getString(14));

                db.insert(TABLE_VALVE_SESN_LOG, null, cv);
            } while (cursor.moveToNext());
        }
        //db.close();
    }

    public int getActiveDvcsOnAddress(String addressUUID) {
        db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.query(TABLE_DVC_MASTER, new String[]{CLM_DVC_IS_SHOW_STATUS}, CLM_ADDRESS_UUID + " = ? AND " + CLM_DVC_IS_SHOW_STATUS + " = ? ", new String[]{addressUUID, "1"}, null, null, null);

        //db.close();
        return cursor.getCount();
    }

    public ArrayList<JSONObject> getListDvcMD() {
        db = this.getWritableDatabase();
        ArrayList<JSONObject> listModalDvcMD = new ArrayList<>();
        Cursor cursor;

        cursor = db.query(TABLE_DVC_MASTER, null, CLM_DVC_OP_TP_INT + " > ? ", new String[]{String.valueOf(0)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ModalDvcMD modalDvcMD = new ModalDvcMD(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getInt(10), cursor.getInt(11), cursor.getString(12), cursor.getString(13));
                listModalDvcMD.add(modalDvcMD.getJSONObjectModal());
            } while (cursor.moveToNext());
        }
        //db.close();
        return listModalDvcMD;
    }

    public ArrayList<JSONObject> getListValveMD() {
        db = this.getWritableDatabase();
        ArrayList<JSONObject> listModalValveMD = new ArrayList<>();
        Cursor cursor;

        cursor = db.query(TABLE_VALVE_MASTER, null, CLM_VALVE_OP_TP_INT + " > ? ", new String[]{String.valueOf(0)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ModalValveMaster modalValveMasterMD = new ModalValveMaster(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getInt(6), cursor.getString(7), cursor.getString(8));
                listModalValveMD.add(modalValveMasterMD.getJsonObjModal());
            } while (cursor.moveToNext());
        }
        //db.close();
        return listModalValveMD;
    }

    public ArrayList<JSONObject> getListValveSessionMD() {
        db = this.getWritableDatabase();
        ArrayList<JSONObject> listValveSessionMD = new ArrayList<>();
        Cursor cursor;

        cursor = db.query(TABLE_VALVE_SESN_MASTER, null, CLM_VALVE_SESN_OP_TP_INT + " > ? ", new String[]{String.valueOf(0)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ModalValveSessionData modalValveSessionData = new ModalValveSessionData(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4), cursor.getInt(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12), cursor.getInt(13), cursor.getString(14));
                listValveSessionMD.add(modalValveSessionData.getJSONObjectModal());
            } while (cursor.moveToNext());
        }
        //db.close();
        return listValveSessionMD;
    }

    public void setOPtoZeroAllMDTables() {
        db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CLM_DVC_OP_TP_INT, 0);
        if (db.update(TABLE_DVC_MASTER, cv, null, null) > 0) {
            cv = new ContentValues();
            cv.put(CLM_VALVE_OP_TP_INT, 0);
            if (db.update(TABLE_VALVE_MASTER, cv, null, null) > 0) {
                cv = new ContentValues();
                cv.put(CLM_VALVE_SESN_OP_TP_INT, 0);
                if (db.update(TABLE_VALVE_SESN_MASTER, cv, null, null) > 0) {
                    Log.e("@@@ OP TYPE ", "MD TABLES OP TY SET TO 0");
                }
            }
        }

        //db.close();
    }

    public int getDvcMasterOpTypeCount() {
        db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.query(TABLE_DVC_MASTER, new String[]{CLM_DVC_OP_TP_INT}, CLM_DVC_OP_TP_INT + " > ? ", new String[]{String.valueOf(0)}, null, null, null);

        ////db.close();
        return cursor.getCount();
    }

    public int getValveMasterOpTypeCount() {
        db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.query(TABLE_VALVE_MASTER, new String[]{CLM_VALVE_OP_TP_INT}, CLM_VALVE_OP_TP_INT + " > ? ", new String[]{String.valueOf(0)}, null, null, null);
        ////db.close();
        return cursor.getCount();
    }

    public int getValveSesnMasterOpTypeCount() {
        db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.query(TABLE_VALVE_SESN_MASTER, new String[]{CLM_VALVE_SESN_OP_TP_INT}, CLM_VALVE_SESN_OP_TP_INT + " > ? ", new String[]{String.valueOf(0)}, null, null, null);
        ////db.close();
        return cursor.getCount();
    }

    public long getDvcLogRowsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_DVC_LOG);
        ////db.close();
        return count;
    }

    public long getValveLogRowsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_VALVE_LOG);
        ////db.close();
        return count;
    }

    public long getValveSesnLogRowsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_VALVE_SESN_LOG);
        ////db.close();
        return count;
    }

    public ArrayList<JSONObject> getListDvcLD() {
        db = getReadableDatabase();
        ArrayList<JSONObject> listDvcLD = new ArrayList<>();
        Cursor cursor = db.query(TABLE_DVC_LOG, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject();

                    jsonObject.put("address_uuid", cursor.getString(0));
                    jsonObject.put("dvc_uuid", cursor.getString(1));
                    jsonObject.put("dvc_name", cursor.getString(2));
                    jsonObject.put("dvc_mac", cursor.getString(3));
                    jsonObject.put("dvc_valve_num", cursor.getInt(4));
                    jsonObject.put("dvc_type", cursor.getString(5) == null ? JSONObject.NULL : cursor.getString(5));
                    jsonObject.put("dvc_qr_code", cursor.getString(6));
                    jsonObject.put("dvc_op_type_aprd_string", cursor.getString(7));
                    jsonObject.put("dvc_op_type_con_discon", cursor.getString(8));
                    jsonObject.put("dvc_last_connected", cursor.getString(9));
                    jsonObject.put("dvc_is_show_status", cursor.getInt(10));
                    jsonObject.put("dvc_op_type_aed", cursor.getInt(11));
                    jsonObject.put("dvc_crted_dt", cursor.getString(12));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                listDvcLD.add(jsonObject);
            }
            while (cursor.moveToNext());
        }
        //db.close();
        return listDvcLD;
    }

    public ArrayList<JSONObject> getListValveLD() {
        db = getReadableDatabase();
        ArrayList<JSONObject> listValveLD = new ArrayList<>();
        Cursor cursor = db.query(TABLE_VALVE_LOG, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject();

                    jsonObject.put("dvc_uuid", cursor.getString(0));
                    jsonObject.put("valve_uuid", cursor.getString(1));
                    jsonObject.put("valve_name", cursor.getString(2));
                    jsonObject.put("valve_select_status", cursor.getInt(3));
                    jsonObject.put("valve_op_ty_spp", cursor.getString(4));
                    jsonObject.put("valve_op_ty_flush_on_off", cursor.getString(5));
                    jsonObject.put("valve_op_ty_int", cursor.getInt(6));
                    jsonObject.put("valve_crt_dt", cursor.getString(7));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                listValveLD.add(jsonObject);
            } while (cursor.moveToNext());
        }
        //db.close();
        return listValveLD;
    }

    public ArrayList<JSONObject> getListValveSessionLD() {
        db = getReadableDatabase();
        ArrayList<JSONObject> listValveSessionLD = new ArrayList<>();
        Cursor cursor = db.query(TABLE_VALVE_SESN_LOG, null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject();

                    jsonObject.put("valve_uuid", cursor.getString(0));
                    jsonObject.put("valve_name_sesn", cursor.getString(1));
                    jsonObject.put("valve_sesn_dp", cursor.getInt(2));
                    jsonObject.put("valve_sesn_duration", cursor.getInt(3));
                    jsonObject.put("valve_sesn_quant", cursor.getInt(4));
                    jsonObject.put("valve_sesn_slot_num", cursor.getInt(5));
                    jsonObject.put("valve_sun_tp", cursor.getString(6) == null ? JSONObject.NULL : cursor.getString(6));
                    jsonObject.put("valve_mon_tp", cursor.getString(7) == null ? JSONObject.NULL : cursor.getString(7));
                    jsonObject.put("valve_tue_tp", cursor.getString(8) == null ? JSONObject.NULL : cursor.getString(8));
                    jsonObject.put("valve_wed_tp", cursor.getString(9) == null ? JSONObject.NULL : cursor.getString(9));
                    jsonObject.put("valve_thu_tp", cursor.getString(10) == null ? JSONObject.NULL : cursor.getString(10));
                    jsonObject.put("valve_fri_tp", cursor.getString(11) == null ? JSONObject.NULL : cursor.getString(11));
                    jsonObject.put("valve_sat_tp", cursor.getString(12) == null ? JSONObject.NULL : cursor.getString(12));
                    jsonObject.put("valve_sesn_op_ty_int", cursor.getInt(13));
                    jsonObject.put("valve_sesn_crt_dt", cursor.getString(14));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                listValveSessionLD.add(jsonObject);
            } while (cursor.moveToNext());
        }
        //db.close();
        return listValveSessionLD;
    }

    public void deleteAllLogsTableData() {
        db = this.getWritableDatabase();

        db.delete(TABLE_DVC_LOG, null, null);
        db.delete(TABLE_VALVE_LOG, null, null);
        db.delete(TABLE_VALVE_SESN_LOG, null, null);

        Log.e("### LOG TABLES ", " DATA DELETED");

        //db.close();
    }

    public void insertAddressModuleFromServer(ModalAddressModule modalAddressModule) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_ADDRESS_UUID, modalAddressModule.getAddressUUID());
        values.put(CLM_ADDRESS_FLAT_HOUSE_BUILDING, modalAddressModule.getFlat_num());
        values.put(CLM_ADDRESS_TOWER_STREET, modalAddressModule.getStreetName());
        values.put(CLM_ADDRESS_AREA_LAND_LOCALITY, modalAddressModule.getLocality_landmark());
        values.put(CLM_ADDRESS_PIN_CODE, modalAddressModule.getPinCode());
        values.put(CLM_ADDRESS_CITY, modalAddressModule.getCity());
        values.put(CLM_ADDRESS_STATE, modalAddressModule.getState());
        values.put(CLM_ADDRESS_IS_SHOW_STATUS, modalAddressModule.getIsShowStatus());
        values.put(CLM_ADDRESS_SELECT_STATUS, modalAddressModule.getIsSelectedStatus());
        values.put(CLM_RADIO_ADDRESS_NAME, modalAddressModule.getAddressRadioName());
        values.put(CLM_ADDRESS_PLACE_LATITUDE, modalAddressModule.getLatitudeLocation());
        values.put(CLM_ADDRESS_PLACE_LONGITUDE, modalAddressModule.getLongitudeLocation());
        values.put(CLM_ADDRESS_PLACE_WELL_KNOWN_NAME, modalAddressModule.getPlaceWellKnownName());
        values.put(CLM_ADDRESS_PLACE_ADDRESS, modalAddressModule.getPlaceAddress());

        db.insert(TABLE_ADDRESS_MASTER, null, values);
        ////db.close();
    }

    public void insertDeviceModuleFromServer(ModalDvcMD modalDvcMD) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_ADDRESS_UUID, modalDvcMD.getAddress_uuid());
        values.put(CLM_DVC_UUID, modalDvcMD.getDvc_uuid());
        values.put(CLM_DVC_NAME, modalDvcMD.getDvc_name());
        values.put(CLM_DVC_MAC, modalDvcMD.getDvc_mac());
        values.put(CLM_DVC_QR_CODE, modalDvcMD.getDvc_qr_code());
        values.put(CLM_DVC_VALVE_NUM, modalDvcMD.getDvc_valve_num());
        values.put(CLM_DVC_TYPE, modalDvcMD.getDvc_type());
        values.put(CLM_DVC_LAST_CONNECTED, modalDvcMD.getDvc_last_connected());
        values.put(CLM_DVC_OP_TP_APRD_STRING, modalDvcMD.getDvc_op_type_aprd_string());
        values.put(CLM_DVC_IS_SHOW_STATUS, modalDvcMD.getDvc_is_show_status());
        values.put(CLM_DVC_OP_TP_CON_DIS_STRING, modalDvcMD.getDvc_op_type_con_discon());
        values.put(CLM_DVC_OP_TP_INT, modalDvcMD.getDvc_op_type_aed());
        values.put(CLM_DVC_CREATED_DT, modalDvcMD.getDvc_crted_dt());
        values.put(CLM_DVC_UPDATED_DT, modalDvcMD.getDvc_updated_dt());

        long insertedRowID = db.insert(TABLE_DVC_MASTER, null, values);
        ////db.close();
    }

    public void insertValveMasterFromServer(ModalValveMaster modalValveMaster) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CLM_DVC_UUID, modalValveMaster.getDvcUUID());
        values.put(CLM_VALVE_UUID, modalValveMaster.getValveUUID());
        values.put(CLM_VALVE_NAME, modalValveMaster.getValveName());
        values.put(CLM_VALVE_SELECT_STATUS, modalValveMaster.getValveSelectStatus());
        values.put(CLM_VALVE_OP_TP_SPP_STRING, modalValveMaster.getValveOpTpSPP());
        values.put(CLM_VALVE_OP_TP_FLASH_ON_OF_STRING, modalValveMaster.getValveOpTpFlushONOFF());
        values.put(CLM_VALVE_OP_TP_INT, modalValveMaster.getValveOpTPInt());
        values.put(CLM_VALVE_CREATED_DT, modalValveMaster.getValveCrtDT());
        values.put(CLM_VALVE_UPDATED_DT, modalValveMaster.getValveUpdateDT());

        db.insert(TABLE_VALVE_MASTER, null, values);
        ////db.close();
    }

    public void insertValveSesnMasterFromServer(ModalValveSessionData modalValveSessionData) {
        ContentValues values = new ContentValues();

        values.put(CLM_VALVE_UUID, modalValveSessionData.getValveUUID());
        values.put(CLM_VALVE_SESN_NAME, modalValveSessionData.getValveNameSession());
        values.put(CLM_VALVE_SESN_DISPOI, modalValveSessionData.getSessionDP());
        values.put(CLM_VALVE_SESN_DURATION, modalValveSessionData.getSessionDuration());
        values.put(CLM_VALVE_SESN_QUANT, modalValveSessionData.getSessionQuantity());
        values.put(CLM_VALVE_SESN_SLOT_NUM, modalValveSessionData.getSesnSlotNum());
        values.put(CLM_VALVE_SESN_DAY_NUM_SUN_TP, modalValveSessionData.getSunTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_MON_TP, modalValveSessionData.getMonTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_TUE_TP, modalValveSessionData.getTueTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_WED_TP, modalValveSessionData.getWedTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_THU_TP, modalValveSessionData.getThuTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_FRI_TP, modalValveSessionData.getFriTP());
        values.put(CLM_VALVE_SESN_DAY_NUM_SAT_TP, modalValveSessionData.getSatTP());
        values.put(CLM_VALVE_SESN_OP_TP_INT, modalValveSessionData.getValveSesnOpTyInt());
        values.put(CLM_VALVE_SESN_CREATED_DT, modalValveSessionData.getValveSesnCrtDT());

        db.insert(TABLE_VALVE_SESN_MASTER, null, values);
    }

    public void closeDB() {
        if (db != null) {
            db.close();
        }
    }

    public String getDvcLastConnected(String dvcUUID) {
        db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_DVC_MASTER, new String[]{CLM_DVC_LAST_CONNECTED}, CLM_DVC_UUID + " = ? AND " + CLM_DVC_IS_SHOW_STATUS + " = ? ", new String[]{dvcUUID, String.valueOf(1)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return "";
    }

    public long entryCountInDvcMaster() {
        db = getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_DVC_MASTER);
    }

    public int updateLastConnected(String dvcUUID, String formattedDate) {
        db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CLM_DVC_OP_TP_CON_DIS_STRING, "Connected");
        cv.put(CLM_DVC_LAST_CONNECTED, formattedDate);
        cv.put(CLM_DVC_OP_TP_INT, 2);
        cv.put(CLM_DVC_UPDATED_DT, getDateTime());

        return db.update(TABLE_DVC_MASTER, cv, CLM_DVC_UUID + " = ? AND " + CLM_DVC_IS_SHOW_STATUS + " = ? ", new String[]{dvcUUID, String.valueOf(1)});
    }
}
