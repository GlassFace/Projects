package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.GroupLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle;

import dataStorageLayer.BookStorageFacade;

import applicationLayer.BranchBook;
import applicationLayer.States;
import applicationLayer.UserControl;


public class BorrowerGUI extends UserGUI{
	
	private static final long serialVersionUID = 1L;
	protected JButton logout;
	protected JLabel userID;
	protected JLabel privilegeType;
	protected JScrollPane booksLentScrollPane;
	protected JScrollPane booksBorrowedScrollPane;
	protected JLabel booksRequestedBack;
	protected JLabel booksLentList;
	protected JLabel bookBorrowedList;
	protected JLabel userName;
	protected JPanel viewStatusPanel;
	protected JButton viewStatus;
	protected JList booksBorrowedListbox;
	protected DefaultListModel booksBorrowedListModel;
	protected DefaultListModel booksRequestedBackListModel;
	protected JList booksRequestedBackListBox;
	
	protected JLabel loginStat;
	protected JLabel userIDvalue;
	protected JLabel userPrivilegeValue;
	protected JLabel userNameValue;
	protected JScrollPane booksRequestedBackScrollPane;

	public BorrowerGUI(String name){
		super();
		initGUI(name);
		this.setVisible(true);
		
	}
	
	//todo this would have to go after this development stage
	public BorrowerGUI(){
		super();
		//todo get rid of this comon
		initGUI("comon");
		//start

		//end
		this.setVisible(true);
	}
	
	private void initGUI(String s){
		logout = new JButton("Logout");
		logout.addActionListener(new LogoutListener());
		
		loginStat = new JLabel("Logged in as: "+s+"      ");
		
		viewStatus = new JButton("View Status");
		viewStatus.addActionListener(new ViewStatusListener());
		west.add(viewStatus);
		viewStatus.setToolTipText("use this to view all information held about you");
		west.add(new JLabel("                "));
		
		south.removeAll();
		south.add(loginStat);
		south.add(logout);
	}
	
	private class LogoutListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			BorrowerGUI.this.dispose();
			//todo show are you sure you want to logout
			//todo add messgae dialog information of logout
			UserGUI g = new UserGUI();
			g.setVisible(true);
		}
	}
	
	private class ViewStatusListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			{
				System.out.println("View Status Button Clicked");
				BorrowerGUI.this.centre.removeAll();
				
				//start
				viewStatusPanel = new JPanel();
				GroupLayout viewStatusPanelLayout = new GroupLayout((JComponent)viewStatusPanel);
				viewStatusPanel.setLayout(viewStatusPanelLayout);
				BorrowerGUI.this.centre.add(viewStatusPanel);
				UserControl uc = UserControl.getInstance();
				viewStatusPanel.setPreferredSize(new java.awt.Dimension(474, 462));
				{
					userID = new JLabel();
					userID.setText("User ID :");
				}
				{
					userName = new JLabel();
					userName.setText("User Name : ");
				}
				{
					privilegeType = new JLabel();
					privilegeType.setText("Privilege : ");
				}
				{
					bookBorrowedList = new JLabel();
					bookBorrowedList.setText("Books Borrowed : ");
				}
				{
					booksLentList = new JLabel();
					booksLentList.setText("Books Lent : ");
				}
				{
					booksRequestedBack = new JLabel();
					booksRequestedBack.setText("Requests For Return : ");
				}
				{
					userIDvalue = new JLabel();
					userIDvalue.setText(String.valueOf(uc.getUser().getId()));
				}
				{
					userNameValue = new JLabel();
					userNameValue.setText(uc.getUser().getFirstName() + " " + uc.getUser().getLastName());
				}
				{
					userPrivilegeValue = new JLabel();
					userPrivilegeValue.setText(uc.getUser().getPrivilege().toString());
				}
				{
					booksBorrowedListModel = new DefaultListModel();
					booksBorrowedListbox = new JList(booksBorrowedListModel);
					booksBorrowedScrollPane = new JScrollPane(booksBorrowedListbox);
					BookStorageFacade bsf = BookStorageFacade.getInstance();
					List<BranchBook> bbl = null;
					try {
						bbl = bsf.getBooksBy("borrower", uc.getUser().getId());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(BorrowerGUI.this, "Unable to retrieve Information from the Database\nError :" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					if (bbl != null) {
						for (BranchBook bb : bbl) {
							booksBorrowedListModel.addElement(bb.getTitle());
						}
					}
				}
				{
					booksRequestedBackListModel = new DefaultListModel();
					booksRequestedBackListBox = new JList(booksRequestedBackListModel);
					booksRequestedBackScrollPane = new JScrollPane(booksRequestedBackListBox);
					BookStorageFacade bsf = BookStorageFacade.getInstance();
					List<BranchBook> bbl = null;
					try {
						bbl = bsf.getBooksBy("borrower", uc.getUser().getId());
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(BorrowerGUI.this, "Unable to retrieve Information from the Database\nError :" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
					if (bbl != null) {
						for (BranchBook bb : bbl) {
							if (bb.getState() == States.REQUESTRETURN)
								booksBorrowedListModel.addElement(bb.getTitle());
						}
					}
				}
				{
					booksLentScrollPane = new JScrollPane();
				}
				viewStatusPanelLayout.setHorizontalGroup(viewStatusPanelLayout.createSequentialGroup()
					.addGap(7)
					.addGroup(viewStatusPanelLayout.createParallelGroup()
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(booksLentList, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					        .addGap(36))
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(privilegeType, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					        .addGap(36))
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(userName, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
					        .addGap(36))
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(userID, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
					        .addGap(42))
					    .addComponent(bookBorrowedList, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(booksRequestedBack, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(viewStatusPanelLayout.createParallelGroup()
					    .addGroup(viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(booksBorrowedScrollPane, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE)
					        .addGap(0, 0, Short.MAX_VALUE))
					    .addGroup(viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(booksRequestedBackScrollPane, GroupLayout.PREFERRED_SIZE, 322, GroupLayout.PREFERRED_SIZE)
					        .addGap(0, 0, Short.MAX_VALUE))
					    .addGroup(viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(booksLentScrollPane, GroupLayout.PREFERRED_SIZE, 325, GroupLayout.PREFERRED_SIZE)
					        .addGap(0, 0, Short.MAX_VALUE))
					    .addGroup(viewStatusPanelLayout.createSequentialGroup()
					        .addPreferredGap(booksBorrowedScrollPane, userIDvalue, LayoutStyle.ComponentPlacement.INDENT)
					        .addGroup(viewStatusPanelLayout.createParallelGroup()
					            .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					                .addComponent(userIDvalue, 0, 263, Short.MAX_VALUE)
					                .addGap(8))
					            .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					                .addComponent(userNameValue, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE)
					                .addGap(0, 20, Short.MAX_VALUE))
					            .addGroup(viewStatusPanelLayout.createSequentialGroup()
					                .addComponent(userPrivilegeValue, GroupLayout.PREFERRED_SIZE, 271, GroupLayout.PREFERRED_SIZE)
					                .addGap(0, 0, Short.MAX_VALUE)))
					        .addGap(42)))
					.addContainerGap());
				viewStatusPanelLayout.setVerticalGroup(viewStatusPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(viewStatusPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					    .addComponent(userID, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					    .addComponent(userIDvalue, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(24)
					.addGroup(viewStatusPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					    .addComponent(userName, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					    .addComponent(userNameValue, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(17)
					.addGroup(viewStatusPanelLayout.createParallelGroup()
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(userPrivilegeValue, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					        .addComponent(privilegeType, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(20)
					.addGroup(viewStatusPanelLayout.createParallelGroup()
					    .addComponent(booksBorrowedScrollPane, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(bookBorrowedList, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
					        .addGap(67)))
					.addGap(34)
					.addGroup(viewStatusPanelLayout.createParallelGroup()
					    .addComponent(booksLentScrollPane, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(booksLentList, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					        .addGap(65)))
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addGroup(viewStatusPanelLayout.createParallelGroup()
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addComponent(booksRequestedBackScrollPane, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
					        .addGap(0, 0, Short.MAX_VALUE))
					    .addGroup(GroupLayout.Alignment.LEADING, viewStatusPanelLayout.createSequentialGroup()
					        .addGap(7)
					        .addComponent(booksRequestedBack, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
					        .addGap(0, 58, Short.MAX_VALUE)))
					.addContainerGap(34, 34));
				//end
			}
		}
	}
	
	
}
