/**
 * Copyright (C) 2013-2014 yitong Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yitong.ChuangKeYuan.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.yitong.ChuangKeYuan.Helper.MyHelper;
import com.easemob.chat.EMContactManager;
import com.yitong.ChuangKeYuan.Helper.MyHelper.DataSyncListener;
import com.yitong.ChuangKeYuan.R;
import com.yitong.ChuangKeYuan.db.InviteMessgeDao;
import com.yitong.ChuangKeYuan.db.UserDao;
import com.yitong.ChuangKeYuan.utils.ToastUtil;
import com.yitong.ChuangKeYuan.widget.ContactItemView;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.ui.EaseContactListFragment;
import com.easemob.util.EMLog;

/**
 * 联系人列表页
 * 
 */
public class ContactListFragment extends EaseContactListFragment {
	
    private static final String TAG = ContactListFragment.class.getSimpleName();
    private ContactSyncListener contactSyncListener;
    private BlackListSyncListener blackListSyncListener;
    private ContactInfoSyncListener contactInfoSyncListener;
    private View loadingView;
    private ContactItemView applicationItem;
    private InviteMessgeDao inviteMessgeDao;

    @Override
    protected void initView() {
        super.initView();
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
//        headerView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
//        headerView.findViewById(R.id.robot_item).setOnClickListener(clickListener);
        //添加headerview
        listView.addHeaderView(headerView);
        //添加正在加载数据提示的loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);
        //注册上下文菜单
        registerForContextMenu(listView);
    }
    
    @Override
    public void refresh() {
        super.refresh();
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if(inviteMessgeDao.getUnreadMessagesCount() > 0){
            applicationItem.showUnreadMsgView();
        }else{
            applicationItem.hideUnreadMsgView();
        }
    }
    
    
    @Override
    protected void setUpView() {
        titleBar.setRightImageResource(R.drawable.jia);
        titleBar.setRightLayoutClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
        
        //设置联系人数据
        setContactsMap(MyHelper.getInstance().getContactList());
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = ((EaseUser)listView.getItemAtPosition(position)).getUsername();
                // demo中直接进入聊天页面，实际一般是进入用户详情页
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
            }
        });

        
        // 进入添加好友页
        titleBar.getRightLayout().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddContactActivity.class));
            }
        });
        
        
        contactSyncListener = new ContactSyncListener();
        MyHelper.getInstance().addSyncContactListener(contactSyncListener);
        
        blackListSyncListener = new BlackListSyncListener();
        MyHelper.getInstance().addSyncBlackListListener(blackListSyncListener);
        
        contactInfoSyncListener = new ContactInfoSyncListener();
        MyHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);
        
        if (!MyHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        } else {
            loadingView.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            MyHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }
        
        if(blackListSyncListener != null){
            MyHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }
        
        if(contactInfoSyncListener != null){
            MyHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
        }
    }
    
	
	protected class HeaderItemClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.application_item:
                // 进入申请与通知页面
                startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                break;
            case R.id.group_item:
                // 进入群聊列表页面
                startActivity(new Intent(getActivity(), GroupsActivity.class));
                break;
            default:
                break;
            }
        }
	    
	}
	

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	    toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
	    toBeProcessUsername = toBeProcessUser.getUsername();
		getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			try {
                // 删除此联系人
                deleteContact(toBeProcessUser);
                // 删除相关的邀请消息
                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                dao.deleteMessage(toBeProcessUser.getUsername());
            } catch (Exception e) {
//                e.printStackTrace();
                return false;
            }
			return true;
		}else if(item.getItemId() == R.id.add_to_blacklist){
			moveToBlacklist(toBeProcessUsername);
			return true;
		}
		return super.onContextItemSelected(item);
	}


	/**
	 * 删除联系人
	 * 
	 * @param
	 */
	public void deleteContact(final EaseUser tobeDeleteUser) {
		String st1 = getResources().getString(R.string.deleting);
		final String st2 = getResources().getString(R.string.Delete_failed);
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage(st1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMContactManager.getInstance().deleteContact(tobeDeleteUser.getUsername());
					// 删除db和内存中此用户的数据
					UserDao dao = new UserDao(getActivity());
					dao.deleteContact(tobeDeleteUser.getUsername());
					MyHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							contactList.remove(tobeDeleteUser);
							contactListLayout.refresh();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
                            ToastUtil.showToast(getActivity(), st2 + e.getMessage());
						}
					});

				}

			}
		}).start();

	}
	
	class ContactSyncListener implements DataSyncListener{
        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(success){
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            }else{
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                ToastUtil.showToast(getActivity(), s1);
                                loadingView.setVisibility(View.GONE);
                            }
                        }
                        
                    });
                }
            });
        }
    }
    
    class BlackListSyncListener implements DataSyncListener{

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    blackList = EMContactManager.getInstance().getBlackListUsernames();
                    refresh();
                }
                
            });
        }
        
    };
    
    class ContactInfoSyncListener implements DataSyncListener{

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if(success){
                        refresh();
                    }
                }
            });
        }
        
    }
	
}
