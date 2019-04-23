
package com.netcommlabs.greencontroller.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netcommlabs.greencontroller.Dialogs.AppAlertDialog;
import com.netcommlabs.greencontroller.R;
import com.netcommlabs.greencontroller.activities.MainActivity;
import com.netcommlabs.greencontroller.adapters.DeviceAddressAdapter;
import com.netcommlabs.greencontroller.constant.TagConstant;
import com.netcommlabs.greencontroller.model.ModalAddressModule;
import com.netcommlabs.greencontroller.model.ModalDeviceModule;
import com.netcommlabs.greencontroller.sqlite_db.DatabaseHandler;
import com.netcommlabs.greencontroller.utilities.BLEAppLevel;
import com.netcommlabs.greencontroller.utilities.MySharedPreference;

import java.util.List;

/**
 * Created by Android on 12/6/2017.
 */
public class FragDeviceMAP extends Fragment implements View.OnClickListener, View.OnLongClickListener {
    private MainActivity mContext;
    private View view;
    private RecyclerView recyclerView;
    private DeviceAddressAdapter mAdapter;
    private LinearLayout llAddNewAddress;
    public LinearLayout llDvcTopContainer, llDvcMiddleContainer, llDvcBottomContainer, llBubbleLeftTopBG, llFooterIM, llBubbleTopRightBG, llBubbleMiddleBG, llBubbleLeftBottomBG, llBubbleRightBottomBG, llNoDeviceIns;
    /* private LinearLayout ll_3st;
     private LinearLayout ll_4st;
     private LinearLayout ll_5st;*/
    private ImageView ivMapNewDevice;
    private RelativeLayout rlBubbleLeftTop, rlBubbleRightTop, rlBubbleMiddle, rlBubbleLeftBottom, rlBubbleRightBottom;
    private String dvcName;
    private String dvcMac;
    private ImageView ivPrev;
    private ImageView ivNext;
    private int valveNum;
    private TextView tvDeviceNameTopLeft, tvValveCountTopLeft, tvDvcNameTopRigh, tvValveCountTopRight, tvDvcNameMiddle, tvValveCountMiddle, tvDvcNameLeftBottom, tvValveCountLeftBottom, tvDvcNameRightBottom, tvValveCountRightBottom;
    private ModalAddressModule modalAddressModule;
    private String addressComplete;
    private List<String> listAddressName;
    private BLEAppLevel bleAppLevel;
    private TextView tvAddressTop, tvEditDvcName, tvPauseDvc, tvResumeDbc, tvConnectDvc, tvDisconnectDvc, tvDeleteDvc, tvEditBtn, tvCancelEdit;
    private DatabaseHandler databaseHandler;
    private String addressUUID = "", dvcUUID = "";
    private int addressSelectStatus;
    private List<ModalDeviceModule> listModalDeviceModule;
    private List<ModalAddressModule> listModalAddressModule;
    private int selectAddressNameListAt;
    public LinearLayout llIMWholeDesign, llDialogLongPressDvc, llDialogEditDvcName;
    private EditText etEditDvcName;
    private int totalPlayValvesCount, totalPauseValvesCount;
    private FragDeviceDetails fragDeviceDetails;
    private Bundle bundle;
    private int dvcBubbleID = 0;
    //private Fragment myFragment;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.device_map, null);

        findViews(view);
        initBase();
        initListeners();

        return view;
    }

    private void findViews(View view) {
        tvAddressTop = mContext.tvDesc_txt;
        recyclerView = view.findViewById(R.id.recyclerView);
        llAddNewAddress = view.findViewById(R.id.llAddNewAddress);
        ivMapNewDevice = view.findViewById(R.id.ivMapNewDevice);
        llBubbleLeftTopBG = view.findViewById(R.id.llBubbleLeftTopBG);
        llBubbleTopRightBG = view.findViewById(R.id.llBubbleTopRightBG);
        llBubbleMiddleBG = view.findViewById(R.id.llBubbleMiddleBG);
        llBubbleLeftBottomBG = view.findViewById(R.id.llBubbleLeftBottomBG);
        llBubbleRightBottomBG = view.findViewById(R.id.llBubbleRightBottomBG);
        rlBubbleLeftTop = view.findViewById(R.id.rlBubbleLeftTop);
        rlBubbleRightTop = view.findViewById(R.id.rlBubbleRightTop);
        rlBubbleMiddle = view.findViewById(R.id.rlBubbleMiddle);
        rlBubbleLeftBottom = view.findViewById(R.id.rlBubbleLeftBottom);
        rlBubbleRightBottom = view.findViewById(R.id.rlBubbleRightBottom);
        llFooterIM = view.findViewById(R.id.llFooterIM);
        ivPrev = view.findViewById(R.id.ivPrev);
        ivNext = view.findViewById(R.id.ivNext);
        tvDeviceNameTopLeft = view.findViewById(R.id.tvDeviceNameTopLeft);
        tvValveCountTopLeft = view.findViewById(R.id.tvValveCountTopLeft);
        tvDvcNameTopRigh = view.findViewById(R.id.tvDvcNameTopRigh);
        tvValveCountTopRight = view.findViewById(R.id.tvValveCountTopRight);
        tvDvcNameMiddle = view.findViewById(R.id.tvDvcNameMiddle);
        tvValveCountMiddle = view.findViewById(R.id.tvValveCountMiddle);
        tvDvcNameLeftBottom = view.findViewById(R.id.tvDvcNameLeftBottom);
        tvValveCountLeftBottom = view.findViewById(R.id.tvValveCountLeftBottom);
        tvDvcNameRightBottom = view.findViewById(R.id.tvDvcNameRightBottom);
        tvValveCountRightBottom = view.findViewById(R.id.tvValveCountRightBottom);
        llDialogLongPressDvc = view.findViewById(R.id.llDialogLongPressDvc);
        llIMWholeDesign = view.findViewById(R.id.llIMWholeDesign);
        tvEditDvcName = view.findViewById(R.id.tvEditDvcName);
        tvPauseDvc = view.findViewById(R.id.tvPauseDvc);
        tvResumeDbc = view.findViewById(R.id.tvResumeDbc);
        tvConnectDvc = view.findViewById(R.id.tvConnectDvc);
        tvDisconnectDvc = view.findViewById(R.id.tvDisconnectDvc);
        tvDeleteDvc = view.findViewById(R.id.tvDeleteDvc);
        llDialogEditDvcName = view.findViewById(R.id.llDialogEditDvcName);
        tvEditBtn = view.findViewById(R.id.tvSaveEditBtn);
        tvCancelEdit = view.findViewById(R.id.tvCancelEdit);
        etEditDvcName = view.findViewById(R.id.etEditDvcName);
        llNoDeviceIns = view.findViewById(R.id.llNoDeviceIns);
        llDvcTopContainer = view.findViewById(R.id.llDvcTopContainer);
        llDvcMiddleContainer = view.findViewById(R.id.llDvcMiddleContainer);
        llDvcBottomContainer = view.findViewById(R.id.llDvcBottomContainer);
    }

    private void initBase() {
        bleAppLevel = BLEAppLevel.getInstanceOnly();
        databaseHandler = DatabaseHandler.getInstance(mContext);
        listModalAddressModule = databaseHandler.getAlladdressUUIDRadioNameSelectStatus();
        if (listModalAddressModule.size() > 0) {
            for (int i = 0; i < listModalAddressModule.size(); i++) {
                if (listModalAddressModule.get(i).getAddressSelectStatus() == 1) {
                    addressUUID = listModalAddressModule.get(i).getAddressUUID();
                    selectAddressNameListAt = i;
                    break;
                } else {
                    addressUUID = listModalAddressModule.get(0).getAddressUUID();
                    selectAddressNameListAt = 0;
                }
            }
        }

        setRecyclerViewAdapter();
        setUIForAddressNdDeviceMap(addressUUID);
    }

    private void initListeners() {
        llAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentTransactions.replaceFragment(mContext, new FragAddEditAddress(), TagConstant.ADD_ADDRESS, mContext.frm_lyt_container_int, true);
            }
        });

        rlBubbleLeftTop.setOnClickListener(this);
        rlBubbleLeftTop.setOnLongClickListener(this);

        rlBubbleRightTop.setOnClickListener(this);
        rlBubbleRightTop.setOnLongClickListener(this);

        rlBubbleMiddle.setOnClickListener(this);
        rlBubbleMiddle.setOnLongClickListener(this);

        rlBubbleLeftBottom.setOnClickListener(this);
        rlBubbleLeftBottom.setOnLongClickListener(this);

        rlBubbleRightBottom.setOnClickListener(this);
        rlBubbleRightBottom.setOnLongClickListener(this);

        ivMapNewDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFragmentTransactions.replaceFragment(mContext, new FragAvailableDevices(), TagConstant.AVAILABLE_DEVICE, mContext.frm_lyt_container_int, true);
            }
        });
        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Previous", Toast.LENGTH_SHORT).show();
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Next", Toast.LENGTH_SHORT).show();
            }
        });

        tvEditDvcName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDialogLongPressDvc.setVisibility(View.GONE);
                llDialogEditDvcName.setVisibility(View.VISIBLE);
                etEditDvcName.setText(dvcName);

                tvCancelEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llIMWholeDesign.setVisibility(View.VISIBLE);
                        llDialogEditDvcName.setVisibility(View.GONE);
                    }
                });

                tvEditBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userIPDvcName = etEditDvcName.getText().toString();
                        if (userIPDvcName.isEmpty()) {
                            Toast.makeText(mContext, "Device name can't be empty", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (userIPDvcName.equals(dvcName)) {
                            Toast.makeText(mContext, "Please edit device name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //No device name should be duplicate
                        if (databaseHandler.getAllDeviceName().size() > 0) {
                            for (int i = 0; i < databaseHandler.getAllDeviceName().size(); i++) {
                                if (databaseHandler.getAllDeviceName().get(i).equalsIgnoreCase(userIPDvcName)) {
                                    Toast.makeText(mContext, "This device name " +
                                            "already exists with app", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                        if (databaseHandler.updateDvcNameOnly(dvcUUID, etEditDvcName.getText().toString()) > 0) {
                            databaseHandler.insertDeviceModuleLog(dvcUUID);
                        }
                        llIMWholeDesign.setVisibility(View.VISIBLE);
                        llDialogEditDvcName.setVisibility(View.GONE);

                        Toast.makeText(mContext, "Device name edited successfully", Toast.LENGTH_SHORT).show();
                        mContext.dvcLongPressEvents();
                    }
                });
            }
        });
        tvPauseDvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleAppLevel = BLEAppLevel.getInstanceOnly();
                if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot()) {
                    dialogPsRmDtConfirm("Pause Device", "This will Pause device completely", "Pause");
                } else {
                    AppAlertDialog.dialogBLENotConnected(mContext, FragDeviceMAP.this, bleAppLevel, dvcMac);
                }
            }
        });
        tvResumeDbc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleAppLevel = BLEAppLevel.getInstanceOnly();
                if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot()) {
                    dialogPsRmDtConfirm("Resume Device", "This will Resume device completely", "Resume");
                } else {
                    AppAlertDialog.dialogBLENotConnected(mContext, FragDeviceMAP.this, bleAppLevel, dvcMac);
                }
            }
        });
        tvConnectDvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleAppLevel = BLEAppLevel.getInstanceOnly();

                if (bleAppLevel == null) {
                    llDialogLongPressDvc.setVisibility(View.GONE);
                    llIMWholeDesign.setVisibility(View.VISIBLE);
                    AppAlertDialog.dialogBLENotConnected(mContext, FragDeviceMAP.this, bleAppLevel, dvcMac);
                }
                /*else {
                    bleAppLevel.disconnectBLECompletely();
                }*/
            }
        });
        tvDisconnectDvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleAppLevel = BLEAppLevel.getInstanceOnly();
                if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot())
                {
                    bleAppLevel.disconnectBLECompletely();
                } else {
                    Toast.makeText(mContext, "BLE Lost connection", Toast.LENGTH_SHORT).show();
                }
                llDialogLongPressDvc.setVisibility(View.GONE);
                llIMWholeDesign.setVisibility(View.VISIBLE);
            }
        });
        tvDeleteDvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bleAppLevel = BLEAppLevel.getInstanceOnly();
                if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot()) {
                    dialogPsRmDtConfirm("Delete Device", "This will Delete device permanently", "Delete");
                } else {
                    AppAlertDialog.dialogBLENotConnected(mContext, FragDeviceMAP.this, bleAppLevel, dvcMac);
                }

            }
        });
    }


    void setRecyclerViewAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new DeviceAddressAdapter(mContext, FragDeviceMAP.this);
        recyclerView.setAdapter(mAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(selectAddressNameListAt + 1);
            }
        }, 200);
    }

    public void setUIForAddressNdDeviceMap(String addressUUID) {
        listModalAddressModule = databaseHandler.getAddressListFormData(addressUUID);
        listModalDeviceModule = databaseHandler.getDeviceDataForIMap(addressUUID);

        modalAddressModule = listModalAddressModule.get(0);
        addressComplete = modalAddressModule.getFlat_num() + ", " + modalAddressModule.getStreetName() + ", " + modalAddressModule.getLocality_landmark() + ", " + modalAddressModule.getPinCode() + ", " + modalAddressModule.getCity() + ", " + modalAddressModule.getState();
        tvAddressTop.setText(addressComplete);

        bleAppLevel = BLEAppLevel.getInstanceOnly();

        if (listModalDeviceModule.size() > 0) {
            llDvcTopContainer.setVisibility(View.VISIBLE);
            llDvcMiddleContainer.setVisibility(View.VISIBLE);
            llDvcBottomContainer.setVisibility(View.VISIBLE);

            llNoDeviceIns.setVisibility(View.GONE);

            for (int i = 0; i < listModalDeviceModule.size(); i++) {
                if (i == 0) {                                               //error: if (rlBubbleLeftTop.getVisibility() == View.GONE && i == 0)
                    dvcName = listModalDeviceModule.get(i).getName();
                    valveNum = listModalDeviceModule.get(i).getValvesNum();
                    dvcMac = listModalDeviceModule.get(i).getDvcMacAddress();

                    rlBubbleLeftTop.setVisibility(View.VISIBLE);
                    rlBubbleRightTop.setVisibility(View.GONE);
                    rlBubbleMiddle.setVisibility(View.GONE);
                    rlBubbleLeftBottom.setVisibility(View.GONE);
                    rlBubbleRightBottom.setVisibility(View.GONE);
                    tvDeviceNameTopLeft.setText(dvcName);
                    tvValveCountTopLeft.setText(valveNum + "");

                    if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot() && dvcMac.equals(bleAppLevel.getDvcAddress())) {
                        //Change background on BLE connected
                        llBubbleLeftTopBG.setBackgroundResource(R.drawable.pebble_back_connected);
                    }
                    else
                        llBubbleLeftTopBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                    continue;
                }
                if (i == 1) {                                           //error: if (rlBubbleRightTop.getVisibility() == View.GONE && i == 1)
                    dvcName = listModalDeviceModule.get(i).getName();
                    valveNum = listModalDeviceModule.get(i).getValvesNum();
                    dvcMac = listModalDeviceModule.get(i).getDvcMacAddress();

                    rlBubbleLeftTop.setVisibility(View.VISIBLE);                    //for i = 1, set visible first device but invisible remaining 4 devices
                    rlBubbleRightTop.setVisibility(View.VISIBLE);
                    rlBubbleMiddle.setVisibility(View.GONE);
                    rlBubbleLeftBottom.setVisibility(View.GONE);
                    rlBubbleRightBottom.setVisibility(View.GONE);
                    tvDvcNameTopRigh.setText(dvcName);
                    tvValveCountTopRight.setText(valveNum + "");

                    if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot() && dvcMac.equals(bleAppLevel.getDvcAddress()))    // check if clicked device is actually connected or not
                    {
                        //Change background on BLE connected
                        llBubbleTopRightBG.setBackgroundResource(R.drawable.pebble_back_connected);
                    }
                    else
                        llBubbleTopRightBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                    continue;
                }

                if (i == 2) {                                                   //error: if (rlBubbleMiddle.getVisibility() == View.GONE && i == 2)
                        dvcName = listModalDeviceModule.get(i).getName();
                    valveNum = listModalDeviceModule.get(i).getValvesNum();
                    dvcMac = listModalDeviceModule.get(i).getDvcMacAddress();

                    rlBubbleLeftTop.setVisibility(View.VISIBLE);                    //for i = 2, set visible first 3 devices bubble but not 4th and 5th
                    rlBubbleRightTop.setVisibility(View.VISIBLE);
                    rlBubbleMiddle.setVisibility(View.VISIBLE);
                    rlBubbleLeftBottom.setVisibility(View.GONE);
                    rlBubbleRightBottom.setVisibility(View.GONE);
                    tvDvcNameMiddle.setText(dvcName);
                    tvValveCountMiddle.setText(valveNum + "");

                if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot() && dvcMac.equals(bleAppLevel.getDvcAddress()))    // check if clicked device is actually connected or not
                    {
                        //Change background on BLE connected
                        llBubbleMiddleBG.setBackgroundResource(R.drawable.pebble_back_connected);
                    }
                    else
                        llBubbleMiddleBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                    continue;
                }
                if (i == 3) {                                                   //error: if (rlBubbleLeftBottom.getVisibility() == View.GONE && i == 3)
                    dvcName = listModalDeviceModule.get(i).getName();
                    valveNum = listModalDeviceModule.get(i).getValvesNum();
                    dvcMac = listModalDeviceModule.get(i).getDvcMacAddress();

                    rlBubbleLeftTop.setVisibility(View.VISIBLE);                 //for i = 3, set visible first 4 devices bubble but not 5th
                    rlBubbleRightTop.setVisibility(View.VISIBLE);
                    rlBubbleMiddle.setVisibility(View.VISIBLE);
                    rlBubbleLeftBottom.setVisibility(View.VISIBLE);
                    rlBubbleRightBottom.setVisibility(View.GONE);
                    tvDvcNameLeftBottom.setText(dvcName);
                    tvValveCountLeftBottom.setText(valveNum + "");

                    if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot() && dvcMac.equals(bleAppLevel.getDvcAddress()))        // check if clicked device is actually connected or not
                    {
                        //Change background on BLE connected
                        llBubbleLeftBottomBG.setBackgroundResource(R.drawable.pebble_back_connected);
                    }
                    else
                        llBubbleLeftBottomBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                    continue;
                }
                if (i == 4) {                                               //error: if (rlBubbleRightBottom.getVisibility() == View.GONE && i == 4)
                    dvcName = listModalDeviceModule.get(i).getName();
                    valveNum = listModalDeviceModule.get(i).getValvesNum();
                    dvcMac = listModalDeviceModule.get(i).getDvcMacAddress();


                    rlBubbleLeftTop.setVisibility(View.VISIBLE);            //for i = 4, enable all 5 device bubbles
                    rlBubbleRightTop.setVisibility(View.VISIBLE);
                    rlBubbleMiddle.setVisibility(View.VISIBLE);
                    rlBubbleLeftBottom.setVisibility(View.VISIBLE);
                    rlBubbleRightBottom.setVisibility(View.VISIBLE);
                    tvDvcNameRightBottom.setText(dvcName);
                    tvValveCountRightBottom.setText(valveNum + "");

                    llFooterIM.setVisibility(View.VISIBLE);

                    if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot() && dvcMac.equals(bleAppLevel.getDvcAddress()))  // check if clicked device is actually connected or not
                    {
                        //Change background on BLE connected
                        llBubbleRightBottomBG.setBackgroundResource(R.drawable.pebble_back_connected);
                    }
                    else
                        llBubbleRightBottomBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                    //To Do if No. of Devices are > 5
                    //continue;
                }
            }
        } else {
            llDvcTopContainer.setVisibility(View.GONE);
            llDvcMiddleContainer.setVisibility(View.GONE);
            llDvcBottomContainer.setVisibility(View.GONE);

            rlBubbleLeftTop.setVisibility(View.GONE);
            rlBubbleRightTop.setVisibility(View.GONE);
            rlBubbleMiddle.setVisibility(View.GONE);
            rlBubbleLeftBottom.setVisibility(View.GONE);
            rlBubbleRightBottom.setVisibility(View.GONE);
            llFooterIM.setVisibility(View.GONE);

            llNoDeviceIns.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.rlBubbleLeftTop:
                //Its data will come from index 0, always
                dvcUUID = listModalDeviceModule.get(0).getDvcUUID();
                dvcName = listModalDeviceModule.get(0).getName();
                dvcMac = listModalDeviceModule.get(0).getDvcMacAddress();
                valveNum = listModalDeviceModule.get(0).getValvesNum();
                break;
            case R.id.rlBubbleRightTop:
                //Its data will come from index 1, always
                dvcUUID = listModalDeviceModule.get(1).getDvcUUID();
                dvcName = listModalDeviceModule.get(1).getName();
                dvcMac = listModalDeviceModule.get(1).getDvcMacAddress();
                valveNum = listModalDeviceModule.get(1).getValvesNum();
                break;
            case R.id.rlBubbleMiddle:
                //Its data will come from index 2, always
                dvcUUID = listModalDeviceModule.get(2).getDvcUUID();
                dvcName = listModalDeviceModule.get(2).getName();
                dvcMac = listModalDeviceModule.get(2).getDvcMacAddress();
                valveNum = listModalDeviceModule.get(2).getValvesNum();
                break;
            case R.id.rlBubbleLeftBottom:
                //Its data will come from index 3, always
                dvcUUID = listModalDeviceModule.get(3).getDvcUUID();
                dvcName = listModalDeviceModule.get(3).getName();
                dvcMac = listModalDeviceModule.get(3).getDvcMacAddress();
                valveNum = listModalDeviceModule.get(3).getValvesNum();
                break;
            case R.id.rlBubbleRightBottom:
                //Its data will come from index 4, always
                dvcUUID = listModalDeviceModule.get(4).getDvcUUID();
                dvcName = listModalDeviceModule.get(4).getName();
                dvcMac = listModalDeviceModule.get(4).getDvcMacAddress();
                valveNum = listModalDeviceModule.get(4).getValvesNum();
                break;
        }
        fragDeviceDetails = new FragDeviceDetails();
        bundle = new Bundle();
        bundle.putString(FragDeviceDetails.EXTRA_DVC_ID, dvcUUID);
        bundle.putString(FragDeviceDetails.EXTRA_DVC_NAME, dvcName);
        bundle.putString(FragDeviceDetails.EXTRA_DVC_MAC, dvcMac);
        bundle.putInt(FragDeviceDetails.EXTRA_DVC_VALVE_COUNT, valveNum);
        fragDeviceDetails.setArguments(bundle);
        //Adding Fragment(FragDeviceDetails)
        MyFragmentTransactions.replaceFragment(mContext, fragDeviceDetails, TagConstant.DEVICE_DETAILS, mContext.frm_lyt_container_int, true);
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rlBubbleLeftTop:
                dvcUUID = listModalDeviceModule.get(0).getDvcUUID();
                dvcMac = listModalDeviceModule.get(0).getDvcMacAddress();
                break;
            case R.id.rlBubbleRightTop:                                     //error: rlBubbleMiddle
                dvcUUID = listModalDeviceModule.get(1).getDvcUUID();
                dvcMac = listModalDeviceModule.get(1).getDvcMacAddress();
                break;
            case R.id.rlBubbleMiddle:                                      //error: rlBubbleRightTop
                dvcUUID = listModalDeviceModule.get(2).getDvcUUID();
                dvcMac = listModalDeviceModule.get(2).getDvcMacAddress();
                break;
            case R.id.rlBubbleLeftBottom:
                dvcUUID = listModalDeviceModule.get(3).getDvcUUID();
                dvcMac = listModalDeviceModule.get(3).getDvcMacAddress();
                break;
            case R.id.rlBubbleRightBottom:
                dvcUUID = listModalDeviceModule.get(4).getDvcUUID();
                dvcMac = listModalDeviceModule.get(4).getDvcMacAddress();
                break;
        }

        dialogLongPressDvc(dvcMac, id);                //send device Mac address to enable/disable particular long pressed device items
        return true;
    }

    public void dialogLongPressDvc(String dvcAddress, int bubbleID) {
        llIMWholeDesign.setVisibility(View.GONE);
        llDialogLongPressDvc.setVisibility(View.VISIBLE);

        if (llDialogLongPressDvc.getVisibility() == View.VISIBLE) {
            bleAppLevel = BLEAppLevel.getInstanceOnly();
            if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot() && dvcAddress.equals(bleAppLevel.getDvcAddress()))      //check for BLE connection with clicked device address
            {
                //Validation Valve PLAY if exists only
                totalPlayValvesCount = databaseHandler.getDvcTotalValvesPlayPauseCount(dvcUUID, "PLAY");
                if (totalPlayValvesCount > 0) {
                    tvPauseDvc.setEnabled(true);
                    tvPauseDvc.setTextColor(Color.BLACK);
                } else {
                    tvPauseDvc.setEnabled(false);
                    tvPauseDvc.setTextColor(Color.GRAY);
                }

                //Validation Valve PAUSE if exists only
                totalPauseValvesCount = databaseHandler.getDvcTotalValvesPlayPauseCount(dvcUUID, "PAUSE");
                if (totalPauseValvesCount > 0) {
                    tvResumeDbc.setEnabled(true);
                    tvResumeDbc.setTextColor(Color.BLACK);
                } else {
                    tvResumeDbc.setEnabled(false);
                    tvResumeDbc.setTextColor(Color.GRAY);
                }

                tvConnectDvc.setEnabled(false);
                tvConnectDvc.setTextColor(Color.GRAY);

                tvDisconnectDvc.setEnabled(true);
                tvDisconnectDvc.setTextColor(Color.BLACK);

                tvDeleteDvc.setEnabled(true);
                tvDeleteDvc.setTextColor(Color.BLACK);

                dvcBubbleID = bubbleID;                //find device id on long press
            }
            else
                {
                tvConnectDvc.setEnabled(true);
                tvConnectDvc.setTextColor(Color.BLACK);

                tvPauseDvc.setEnabled(false);
                tvResumeDbc.setEnabled(false);
                tvDisconnectDvc.setEnabled(false);
                tvDeleteDvc.setEnabled(false);

                tvPauseDvc.setTextColor(Color.GRAY);
                tvResumeDbc.setTextColor(Color.GRAY);
                tvDisconnectDvc.setTextColor(Color.GRAY);
                tvDeleteDvc.setTextColor(Color.GRAY);

                /*if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot()) {
                    bleAppLevel.disconnectBLECompletely();
                }*/
                dvcBubbleID = bubbleID;
            }

        }
    }

    private void dialogPsRmDtConfirm(String title, String msg, final String positiveBtnName) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(mContext);

        builder.setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(positiveBtnName, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BLEAppLevel bleAppLevel = BLEAppLevel.getInstanceOnly();
                        if (bleAppLevel != null && bleAppLevel.getBLEConnectedOrNot()) {
                            if (positiveBtnName.equals("Pause")) {
                                bleAppLevel.cmdDvcPause(FragDeviceMAP.this, "PAUSE", totalPlayValvesCount);
                            } else if (positiveBtnName.equals("Resume")) {
                                bleAppLevel.cmdDvcPlay(FragDeviceMAP.this, "PLAY", totalPauseValvesCount);
                            } else if (positiveBtnName.equals("Delete")) {
                                int totalPlayPauseValvesCount = databaseHandler.getDvcTotalValvesPlayPauseCount(dvcUUID, "STOP");
                                bleAppLevel.cmdDvcStop(FragDeviceMAP.this, "STOP", totalPlayPauseValvesCount);

                                //databaseHandler.deleteUpdateDevice()
                            }
                        } else {
                            Toast.makeText(mContext, "BLE lost connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        /*llDialogLongPressDvc.setVisibility(View.GONE);
                        llIMWholeDesign.setVisibility(View.VISIBLE);*/
                    }
                });
        //.show();
        AlertDialog alert = builder.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alert.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        alert.getWindow().setBackgroundDrawableResource(R.color.theme_color);
        alert.show();
    }

    public void dvcLongPressBLEDone(String cmdTypeName) {
        if (cmdTypeName.equals("PAUSE")) {
            if (databaseHandler.updateValveOpTpSPPStatus(dvcUUID, "", "PAUSE") > 0) {
                databaseHandler.insertValveMasterLog(dvcUUID);
            }
            if (databaseHandler.updateDvcOpTyStringAll(dvcUUID, "PAUSE") > 0) {
                databaseHandler.insertDeviceModuleLog(dvcUUID);
            }
            Toast.makeText(mContext, "Device paused successfully", Toast.LENGTH_SHORT).show();
        } else if (cmdTypeName.equals("PLAY")) {
            if (databaseHandler.updateValveOpTpSPPStatus(dvcUUID, "", "PLAY") > 0) {
                databaseHandler.insertValveMasterLog(dvcUUID);
            }
            if (databaseHandler.updateDvcOpTyStringAll(dvcUUID, "RESUME") > 0) {
                databaseHandler.insertDeviceModuleLog(dvcUUID);
            }
            Toast.makeText(mContext, "Device resumed successfully", Toast.LENGTH_SHORT).show();
        } else if (cmdTypeName.equals("STOP")) {
            if (databaseHandler.updateValveOpTpSPPStatus(dvcUUID, "", "STOP") > 0) ;
            {
                databaseHandler.insertValveMasterLog(dvcUUID);
            }
            int rowAffected = databaseHandler.deleteUpdateDevice(dvcUUID);
            if (rowAffected > 0) {
                mContext.dvcDeleteUpdateSuccess();
                if (databaseHandler.updateDvcOpTyStringAll(dvcUUID, "Disconnected") > 0) {
                    databaseHandler.insertDeviceModuleLog(dvcUUID);
                }
            }
        }

        llDialogLongPressDvc.setVisibility(View.GONE);
        llIMWholeDesign.setVisibility(View.VISIBLE);
    }

    public void BLEConnectedACK() {
        long entryCount = databaseHandler.entryCountInDvcMaster();
        if (entryCount > 0) {
            if (databaseHandler.updateLastConnected(dvcUUID, MySharedPreference.getInstance(mContext).getLastConnectedTime()) > 0) {
                databaseHandler.insertDeviceModuleLog(dvcUUID);
            }
        }
        switch (dvcBubbleID)
        {
            case R.id.rlBubbleLeftTop:
                llBubbleLeftTopBG.setBackgroundResource(R.drawable.pebble_back_connected);
                break;

            case R.id.rlBubbleRightTop:
                llBubbleTopRightBG.setBackgroundResource(R.drawable.pebble_back_connected);
                break;

            case R.id.rlBubbleMiddle:
                llBubbleMiddleBG.setBackgroundResource(R.drawable.pebble_back_connected);
                break;

            case R.id.rlBubbleLeftBottom:
                llBubbleLeftBottomBG.setBackgroundResource(R.drawable.pebble_back_connected);
                break;

            case R.id.rlBubbleRightBottom:
                llBubbleRightBottomBG.setBackgroundResource(R.drawable.pebble_back_connected);
                break;
        }
        //llBubbleLeftTopBG.setBackgroundResource(R.drawable.pebble_back_connected);
    }

    public void disconnectCallBack()
    {
        if (databaseHandler.updateDvcOpTyStringAll(dvcUUID, "Disconnected") > 0) {
            databaseHandler.insertDeviceModuleLog(dvcUUID);
        }
        //llBubbleLeftTopBG.setBackgroundResource(R.drawable.round_back_shadow_small);
        switch (dvcBubbleID)
        {
            case R.id.rlBubbleLeftTop:
                llBubbleLeftTopBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                break;
            case R.id.rlBubbleRightTop:
                llBubbleTopRightBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                break;
            case R.id.rlBubbleMiddle:
                llBubbleMiddleBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                break;
            case R.id.rlBubbleLeftBottom:
                llBubbleLeftBottomBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                break;
            case R.id.rlBubbleRightBottom:
                llBubbleRightBottomBG.setBackgroundResource(R.drawable.round_back_shadow_small);
                break;
        }
    }
}
