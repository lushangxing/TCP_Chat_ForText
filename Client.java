package messenger;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Client extends JFrame{
	   private Socket socket;
	    private DataInputStream datainputstream;
	    private DataOutputStream dataoutputstream;
	    private boolean ClientStatus;
	    private boolean ServerStatus;
	    private javax.swing.JTextArea Chat;
	    private javax.swing.JButton ConnectButton;
	    private javax.swing.JTextField ConnectionStatus;
	    private javax.swing.JTextField IP_Port;
	    private javax.swing.JLabel Label;
	    private javax.swing.JButton SendButton;
	    private javax.swing.JTextArea conversation;
	    private javax.swing.JScrollPane jScrollPane1;
	    private javax.swing.JScrollPane jScrollPane2;
	    
	    public Client() {
	        initComponents();
	        this.setBounds(700,100,520,420);
	        ClientStatus = false;	//��־Client�Ƿ��ϴη���
	        ServerStatus = false;	//��־Server�Ƿ��ϴη���
	    }

	    private void Initialize()
	    {
	        try
	        {
	            datainputstream = new DataInputStream(socket.getInputStream());
	            dataoutputstream = new DataOutputStream(socket.getOutputStream());
	        }
	        catch(Exception ex)
	        {
	            ConnectionStatus.setText("                         Not Connected");
	        }
	    }

	    private String GetTime()	//��÷���ʱ��
	    {
	        Calendar cal = Calendar.getInstance();
	        cal.getTime();
	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");	//��ʽΪʱ���֣���
	        return sdf.format(cal.getTime());
	    }    

	
	    @SuppressWarnings("unchecked")
	    private void initComponents() {
	        Label = new javax.swing.JLabel();
	        IP_Port = new javax.swing.JTextField();	//�������ӵĶ˿ں�
	        ConnectButton = new javax.swing.JButton();	//���Ӱ�ť
	        jScrollPane2 = new javax.swing.JScrollPane();	
	        conversation = new javax.swing.JTextArea();		//�����¼��
	        jScrollPane1 = new javax.swing.JScrollPane();
	        Chat = new javax.swing.JTextArea();	//�����ı���
	        SendButton = new javax.swing.JButton();	//���Ͱ�ť
	        ConnectionStatus = new javax.swing.JTextField();	//�������ӵ�Socket

	        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
	        setTitle("Client");
	        setResizable(false);
	        getContentPane().setLayout(null);
	        
	        //���Socket������
	        Label.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); 
	        Label.setText("IP & Port:");
	        getContentPane().add(Label);
	        Label.setBounds(40, 40, 130, 30);

	        IP_Port.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
	        IP_Port.setText("localhost:1234");
	        getContentPane().add(IP_Port);
	        IP_Port.setBounds(130, 40, 180, 30);

	        ConnectButton.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
	        ConnectButton.setText("Connect");
	        ConnectButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                ConnectButtonActionPerformed(evt);
	            }
	        });
	        getContentPane().add(ConnectButton);
	        ConnectButton.setBounds(340, 40, 130, 30);
	        
	        //��������
	        conversation.setEditable(false);	//���������¼��ֻ����д
	        conversation.setColumns(20);
	        conversation.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
	        conversation.setRows(5);
	        conversation.setEnabled(false);
	        jScrollPane2.setViewportView(conversation);	//����������ڹ��������

	        getContentPane().add(jScrollPane2);
	        jScrollPane2.setBounds(10, 80, 490, 250);
	        
	        //����ı�������
	        Chat.setColumns(20);
	        Chat.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
	        Chat.setRows(1);
	        jScrollPane1.setViewportView(Chat);

	        getContentPane().add(jScrollPane1);
	        jScrollPane1.setBounds(10, 340, 400, 40);

	        SendButton.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
	        SendButton.setText("Send");
	        SendButton.addActionListener(new java.awt.event.ActionListener() {
	            public void actionPerformed(java.awt.event.ActionEvent evt) {
	                SendButtonActionPerformed(evt);
	            }
	        });
	        getContentPane().add(SendButton);
	        SendButton.setBounds(430, 340, 60, 40);

	        ConnectionStatus.setEditable(false);
	        ConnectionStatus.setFont(new java.awt.Font("Comic Sans MS", 0, 12)); // NOI18N
	        ConnectionStatus.setText("                    Not Connected");	//ʹ��JTextfield,�����л�Ϊ��Connected"
	        ConnectionStatus.setEnabled(false);
//	        ConnectionStatus.addActionListener(new java.awt.event.ActionListener() {
//	            public void actionPerformed(java.awt.event.ActionEvent evt) {
//	                ConnectionStatusActionPerformed(evt);
//	            }
//	        });
	        getContentPane().add(ConnectionStatus);
	        ConnectionStatus.setBounds(110, 10, 240, 20);

	        pack();	//
	    }
	    /*
	     * ����ConnectButton��������Serverȡ��ͨ��
	     */
	    private void ConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {
	        new Thread(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                String[] temp = IP_Port.getText().split(":");
	                try
	                {
	                    socket = new Socket(temp[0],Integer.parseInt(temp[1]));
	                    Initialize();	//����socket�����������
	                    ConnectionStatus.setText("                      Connected");
	                    StartRecieving();	//��ʱ׼������Server�˵���Ϣ
	                }
	                catch(Exception ex)
	                {
	                    ConnectionStatus.setText("                    Not Connected");	//��׽�쳣������ʧ��
	                }
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
	                    dataoutputstream.flush();
	                    if(ClientStatus == false)
	                    {
	                        conversation.append("\n Client: \n [" + GetTime() +"]  " + Chat.getText());
	                        ClientStatus = true;
	                    }
	                    else
	                    {
	                        conversation.append("\n [" + GetTime() + "]  " + Chat.getText());
	                    }
	                    ServerStatus = false;
	                    Chat.setText("");
	                }
	                catch(IOException ex)
	                {
	                }
	            }
	        }).start();
	    }

 	    /*
	     * ����Server���͵��ı����ݣ���ӡ������Ϣ
	     */
	    private void StartRecieving()
	    {
	        new Thread(new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                String text;
	                while(true)
	                {
	                    try
	                    {
	                        text = datainputstream.readUTF();
	                        ClientStatus = false;
	                        if(ServerStatus == false)
	                        {
	                           conversation.append("\n Server: \n [" + GetTime() + "]  " + text);
	                           ServerStatus = true;
	                        }
	                        else
	                        {
	                           conversation.append("\n [" + GetTime() + "]  " + text);
	                        }
	                    }
	                    catch (IOException ex)
	                    {
	                    }
	                }
	            }
	        }).start();
	    }
}
