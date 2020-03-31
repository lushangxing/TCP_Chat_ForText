package messenger;


import javax.swing.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.awt.*;
import java.util.*;
import java.util.Calendar;

public class Server extends JFrame{
	private ServerSocket serversocket;
    private Socket socket;
    private DataInputStream datainputstream;
    private DataOutputStream dataoutputstream;
    private boolean ClientStatus;
    private boolean ServerStatus;
    private javax.swing.JTextArea Chat;
    private javax.swing.JTextArea Conversation;
    private javax.swing.JLabel Error;
    private javax.swing.JLabel Label;
    private javax.swing.JTextField PortNo;
    private javax.swing.JButton SendButton;
    private javax.swing.JButton StartListening;	
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;

    public Server()
    {
        initComponents();
        this.setBounds(50,100,520,420);
        DisableFields();
        Error.setForeground(Color.red);
        ClientStatus = false;
        ServerStatus = false;
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Label = new javax.swing.JLabel();	//��ʾport�����ǩ
        StartListening = new javax.swing.JButton();	//����Socket������ť
        jScrollPane1 = new javax.swing.JScrollPane();
        Chat = new javax.swing.JTextArea();		//�����ı���
        jScrollPane2 = new javax.swing.JScrollPane();		
        Conversation = new javax.swing.JTextArea();	//�����¼�ı���
        SendButton = new javax.swing.JButton();	//�����ı���ť
        PortNo = new javax.swing.JTextField();	//�����˿ں�
        Error = new javax.swing.JLabel();	//��ʾ��������˿���ʾ��ǩ

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server");
        setResizable(false);
        getContentPane().setLayout(null);

        Label.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
        Label.setText("Port Number: ");
        getContentPane().add(Label);
        Label.setBounds(50, 30, 130, 30);

        StartListening.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        StartListening.setText("Start Listening");
        StartListening.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartListeningActionPerformed(evt);
            }
        });
        getContentPane().add(StartListening);
        StartListening.setBounds(340, 30, 130, 30);

        Chat.setColumns(20);
        Chat.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        Chat.setRows(1);
        jScrollPane1.setViewportView(Chat);	//��chat�����ı�������

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 340, 410, 40);

        Conversation.setEditable(false);
        Conversation.setColumns(20);
        Conversation.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        Conversation.setRows(5);
        Conversation.setEnabled(false);
        jScrollPane2.setViewportView(Conversation);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(10, 70, 490, 260);

        SendButton.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        SendButton.setText("Send");
        SendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendButtonActionPerformed(evt);
            }
        });
        getContentPane().add(SendButton);
        SendButton.setBounds(430, 340, 60, 40);

        PortNo.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        PortNo.setText("1234");
        getContentPane().add(PortNo);
        PortNo.setBounds(160, 30, 156, 30);

        Error.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
        getContentPane().add(Error);
        Error.setBounds(40, 10, 280, 20);

        pack();
    }

    /*
     * 
     */
    private void StartListeningActionPerformed(java.awt.event.ActionEvent evt) {//����Starting Listening��ť�����Կ���IP_Port��ʹ��ΪListennig״̬
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {	//�жϼ������Ķ˿�Ϊ�ǹ��϶˿ںţ�����0-1023
                String Port = PortNo.getText();
                int PortNumber;
                if(Port.length() >= 4)
                {
                    try
                    {
                        PortNumber = Integer.parseInt(Port);
                        if(PortNumber >= 1024)
                        {
                            Error.setText("");
                            Initialize(PortNumber);
                            StartRecieving();
                            EnableFields();
                        }
                        else
                        {
                            Error.setText("Wrong Port Number");	//PortNumberΪ���϶˿ںţ�����ʧ��
                        }
                    }
                    catch(Exception ex)
                    {
                    }
                }
                else
                {
                    Error.setText("Wrong Port Number Length"); 	//��׽�쳣������PortNumberʧ��
                    DisableFields();
                }
            }
            /*
             * ����serversocket���������
             */
            private void Initialize(int portNumber) throws IOException
            {
                serversocket = new ServerSocket(portNumber);
                socket = serversocket.accept();
                datainputstream = new DataInputStream(socket.getInputStream());
                dataoutputstream = new DataOutputStream(socket.getOutputStream());
            }
        }).start();
    }
    /*
     * ����SendButton��ť�������ı�����dataoutputstream����ӡ������Ϣ
     */
    private void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendButtonActionPerformed
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    dataoutputstream.writeUTF(Chat.getText());
                    if(ServerStatus == false)
                    {
                        Conversation.append("\n Server: \n [" + GetTime() + "]  " + Chat.getText());
                        ServerStatus = true;
                    }
                    else
                    {
                        Conversation.append("\n [" + GetTime() + "]  " + Chat.getText());
                    }
                    ClientStatus = false;
                    serversocket.close();
                    Chat.setText("");
                }
                catch(IOException ex)
                {
                }
            }
        }).start();
    }//GEN-LAST:event_SendButtonActionPerformed

    private void StartRecieving()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String text = "";
                while(true)
                {
                    try
                    {
                        text = datainputstream.readUTF();
                        ServerStatus = false;
                        if(ClientStatus == false)
                        {
                            Conversation.append("\n Client: \n [" + GetTime() + "]  " + text);
                            ClientStatus = true;
                        }
                        else
                        {
                            Conversation.append("\n [" + GetTime() + "]  " + text);
                        }
                        EnableFields();
                    }
                    catch (IOException ex)
                    {
                    }
                }
            }
        }).start();
    }

    private void EnableFields()
    {
        Chat.setEnabled(true);
        SendButton.setEnabled(true);
    }

    private void DisableFields()
    {
        Chat.setEnabled(false);
        SendButton.setEnabled(false);
    }

    private String GetTime()
    {
        Calendar cal = Calendar.getInstance();
        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
