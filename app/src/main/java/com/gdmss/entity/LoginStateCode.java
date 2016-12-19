package com.gdmss.entity;

import com.gdmss.R;
import android.content.Context;



/**
 * ��½״̬��
 * 
 * @author Simula
 * 
 */
public class LoginStateCode
{
    public final static int LOGIN_OK = 0;// ��½�ɹ�
    
    public final static int NPC_D_MPI_MON_ERROR_SYS_ERROR = 1; // ϵͳ����
    
    public final static int NPC_D_MPI_MON_ERROR_CONNECT_FAIL = 2; // ����ʧ��
    
    public final static int NPC_D_MPI_MON_ERROR_DBACCESS_FAIL = 3; // �������ݿ�ʧ��
    
    public final static int NPC_D_MPI_MON_ERROR_ALLOC_RES_FAIL = 4; // ������Դʧ��
    
    public final static int NPC_D_MPI_MON_ERROR_INNER_OP_FAIL = 5; // �ڲ�����ʧ�ܣ����ڴ����ʧ��
    
    public final static int NPC_D_MPI_MON_ERROR_EXEC_ORDER_CALL_FAIL = 6; // ִ���������ʧ��
    
    public final static int NPC_D_MPI_MON_ERROR_EXEC_ORDER_RET_FAIL = 7; // ִ��������ʧ��
    
    public final static int NPC_D_MPI_MON_ERROR_FILE_NONENTITY = 8; // �ļ�������
    
    public final static int NPC_D_MPI_MON_ERROR_OTHER_FAIL = 9; // ����ԭ��ʧ��
    
    public final static int NPC_D_MPI_MON_ERROR_NET_ERROR = 10; // �������
    
    public final static int NPC_D_MPI_MON_ERROR_REDIRECT = 11; // ����������ͻ����ض���
    
    public final static int NPC_D_MPI_MON_ERROR_PARAM_ERROR = 12; // ���������ʽ����
    
    public final static int NPC_D_MPI_MON_ERROR_ERROR_FUNCID = 13; // ����Ĺ���ID��Ϣ
    
    public final static int NPC_D_MPI_MON_ERROR_MSG_PAST_TIME = 14; // ����ϢID�ѹ�ʱ����������
    
    public final static int NPC_D_MPI_MON_ERROR_SYS_NO_GRANT = 15; // ϵͳδ��Ȩ
    
    public final static int NPC_D_MPI_MON_ERROR_USERID_ERROR = 101; // �û�ID���û�������
    
    public final static int NPC_D_MPI_MON_ERROR_USERPWD_ERROR = 102; // �û��������
    
    public final static int NPC_D_MPI_MON_ERROR_USER_PWD_ERROR = 103; // �û������������
    
    public final static int NPC_D_MPI_MON_ERROR_CONNECTING = 104; // ��������
    
    public final static int NPC_D_MPI_MON_ERROR_CONNECTED = 105; // ������
    
    public final static int NPC_D_MPI_MON_ERROR_PLAY_FAIL = 106; // ����ʧ��
    
    public final static int NPC_D_MPI_MON_ERROR_NO_CONNECT_CAMERA = 107; // δ���������
    
    public final static int NPC_D_MPI_MON_ERROR_PLAYING = 108; // ���ڲ���
    
    public final static int NPC_D_MPI_MON_ERROR_NO_PLAY = 109; // δ����
    
    public final static int NPC_D_MPI_MON_ERROR_NONSUP_VENDOR = 110; // ��֧�ֵĳ���
    
    public final static int NPC_D_MPI_MON_ERROR_REJECT_ACCESS = 111; // Ȩ�޲���
    
    public final static int NPC_D_MPI_MON_ERROR_CAMERA_OFFLINE = 112; // ���������
    
    public final static int NPC_D_MPI_MON_ERROR_ACCOUNT_LOGINED = 113; // �ʺ��ѵ�¼
    
    public final static int NPC_D_MPI_MON_ERROR_ACCOUNT_HAVE_EXPIRED = 114; // �û��ʺ��ѹ���Ч��
    
    public final static int NPC_D_MPI_MON_ERROR_ACCOUNT_NO_ACTIVE = 115; // �û��ʺ�δ����
    
    public final static int NPC_D_MPI_MON_ERROR_ACCOUNT_DEBT_STOP = 116; // �û��ʺ���Ƿ��ͣ��
    
    public final static int NPC_D_MPI_MON_ERROR_USER_EXIST = 117; // �û���ע��
    
    public final static int NPC_D_MPI_MON_ERROR_NOT_ALLOW_REG_NOPERM = 118; // ������ע�ᣨ������ɱ��У�
    
    public final static int NPC_D_MPI_MON_ERROR_NOT_ALLOW_REG_ATBLACK = 119; // ������ע�ᣨ�ں������У�
    
    public final static int NPC_D_MPI_MON_ERROR_SECCODE_HAVE_EXPIRED = 120; // ��֤���ѹ���
    
    public final static int NPC_D_MPI_MON_ERROR_SECCODE_ERROR = 121; // ��֤�����
    
    public final static int NPC_D_MPI_MON_ERROR_ACCOUNT_EXIST = 122; // �ʺ��Ѵ���
    
    public final static int NPC_D_MPI_MON_ERROR_NO_IDLE_STREAMSERVER = 123; // �޿�����ý�������
    
    public final static int NPC_D_MPI_MON_ERROR_USER_NO_LOGIN = 124; // �û�δ��¼
    
    public final static int NPC_D_MPI_MON_ERROR_ACCOUNT_LEN_ERROR = 125; // �ʺų��ȴ���
    
    public final static int NPC_D_MPI_MON_ERROR_EMP_ACC_USERID_NOT_EXIST = 126; // ������Ȩ���û�ID������
    
    public final static int NPC_D_MPI_MON_ERROR_IPADDR_BAN_LOGIN = 127; // IP��ַ��ֹ��¼
    
    public final static int NPC_D_MPI_MON_ERROR_CLIENTID_NOT_ALLOW_LOGIN = 128; // �ͻ���ID�������¼
    
    public final static int NPC_D_MPI_MON_ERROR_TIMESECT_NOT_ALLOW_CAMERA = 129; // ��ʱ��β�������ʸ������
    
    /**
     * 获取登陆返回的状态说明码
     * 
     * @param state
     * @param context
     * @return
     */
    public static String GetDes(int state, Context context)
    {
        String des = context.getString(R.string.note_login_again);
        switch (state)
        {
            case NPC_D_MPI_MON_ERROR_CLIENTID_NOT_ALLOW_LOGIN:
                // des=context.getString(R.string.no_right_login);
                break;
            case LOGIN_OK:
                des = context.getString(R.string.LOGIN_OK);
                break;
            case NPC_D_MPI_MON_ERROR_CONNECT_FAIL:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_CONNECT_FAIL);
                break;
            case NPC_D_MPI_MON_ERROR_USERID_ERROR:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_USERID_ERROR);
                break;
            case NPC_D_MPI_MON_ERROR_USERPWD_ERROR:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_USERPWD_ERROR);
                break;
            case NPC_D_MPI_MON_ERROR_USER_PWD_ERROR:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_USER_PWD_ERROR);
                break;
            case NPC_D_MPI_MON_ERROR_CONNECTING:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_CONNECTING);
                break;
            case NPC_D_MPI_MON_ERROR_CONNECTED:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_CONNECTED);
                break;
            case NPC_D_MPI_MON_ERROR_PLAY_FAIL:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_PLAY_FAIL);
                break;
            case NPC_D_MPI_MON_ERROR_FILE_NONENTITY:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_FILE_NONENTITY);
                break;
            case NPC_D_MPI_MON_ERROR_EXEC_ORDER_CALL_FAIL:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_EXEC_ORDER_CALL_FAIL);
                break;
            case NPC_D_MPI_MON_ERROR_EXEC_ORDER_RET_FAIL:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_EXEC_ORDER_RET_FAIL);
                break;
            case NPC_D_MPI_MON_ERROR_ALLOC_RES_FAIL:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_ALLOC_RES_FAIL);
                break;
            case NPC_D_MPI_MON_ERROR_NO_CONNECT_CAMERA:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_NO_CONNECT_CAMERA);
                break;
            case NPC_D_MPI_MON_ERROR_INNER_OP_FAIL:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_INNER_OP_FAIL);
                break;
            case NPC_D_MPI_MON_ERROR_PLAYING:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_PLAYING);
                break;
            case NPC_D_MPI_MON_ERROR_NO_PLAY:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_NO_PLAY);
                break;
            case NPC_D_MPI_MON_ERROR_NONSUP_VENDOR:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_NONSUP_VENDOR);
                break;
            case NPC_D_MPI_MON_ERROR_REJECT_ACCESS:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_REJECT_ACCESS);
                break;
            case NPC_D_MPI_MON_ERROR_CAMERA_OFFLINE:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_CAMERA_OFFLINE);
                break;
            case NPC_D_MPI_MON_ERROR_ACCOUNT_LOGINED:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_ACCOUNT_LOGINED);
                break;
            case NPC_D_MPI_MON_ERROR_ACCOUNT_HAVE_EXPIRED:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_ACCOUNT_NO_ACTIVE);
                break;
            case NPC_D_MPI_MON_ERROR_ACCOUNT_NO_ACTIVE:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_CONNECTING);
                break;
            case NPC_D_MPI_MON_ERROR_ACCOUNT_DEBT_STOP:
                des = context.getString(R.string.NPC_D_MPI_MON_ERROR_ACCOUNT_DEBT_STOP);
                break;
            default:
                des = context.getString(R.string.note_login_again);
                break;
        }
        
        return des;
    }
}
