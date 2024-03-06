package mvc;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Color;
//import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JMenuItem;

public class DrawingFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DrawingView view = new DrawingView();
	private DrawingController controller;

	public JButton getBtnUndo() {
		return btnUndo;
	}

	public JButton getBtnRedo() {
		return btnRedo;
	}
	
	ImageIcon undoIcon = new ImageIcon(".\\lib\\undo.png");
	

	private JPanel contentPane;
	private JToolBar toolBar;
	private JToggleButton tglbtnSelection;
	private JToggleButton tglbtnPoint;
	private JToggleButton tglbtnLine;
	private JToggleButton tglbtnRectangle;
	private JToggleButton tglbtnCircle;
	private JToggleButton tglbtnDonut;
	private ButtonGroup group = new ButtonGroup();

	private JToggleButton tglbtnModify;
	private JToggleButton tglbtnDelete;
	private JToggleButton tglbtnHexagon;

	private JToolBar toolBarSide;
	private JButton btnInnerColor;
	private JButton btnOutterColor;
	private JMenuBar menuBar;
	private JMenu mnLog;
	private JButton btnUndo;
	private JButton btnRedo;
	private JScrollPane scrollPane;
	private JList<String> list;
	public DefaultListModel<String> defaultListModel = new DefaultListModel<String>();
	private JButton btnToBack;
	private JButton btnToFront;
	private JButton btnBringToBack;
	private JButton btnBringToFront;
	private JMenuItem mntmSaveLog;
	private JMenu mnDrawing;
	private JMenuItem mntmLoadLog;
	private JMenuItem mntmSaveDrawing;
	private JMenuItem mntmLoadDrawing;
	private JButton btnLoadNext;

	public DrawingView getView() {
		return view;
	}

	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	public DrawingFrame() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		setTitle("Ćetković Vlado IT29/2018");
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnLog = new JMenu("Log");
		menuBar.add(mnLog);
		
		mntmSaveLog = new JMenuItem("Save Log");
		mntmSaveLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.saveLog();
			}
		});
		mnLog.add(mntmSaveLog);
		
		mntmLoadLog = new JMenuItem("Load Log");
		mntmLoadLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.loadLog();
			}
		});
		mnLog.add(mntmLoadLog);
		
		mnDrawing = new JMenu("Drawing");
		menuBar.add(mnDrawing);
		
		mntmSaveDrawing = new JMenuItem("Save Drawing");
		mntmSaveDrawing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.saveDrawing();
			}
		});
		mnDrawing.add(mntmSaveDrawing);
		
		mntmLoadDrawing = new JMenuItem("Load Drawing");
		mntmLoadDrawing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controller.loadDrawing();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnDrawing.add(mntmLoadDrawing);
		
		btnLoadNext = new JButton("Load next");
		btnLoadNext.setVisible(false);
		btnLoadNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.loadNext();
			}
		});
		menuBar.add(btnLoadNext);

		contentPane = new JPanel();

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);


		tglbtnSelection = new JToggleButton("Selection");
		toolBar.add(tglbtnSelection);
		group.add(tglbtnSelection);

		tglbtnPoint = new JToggleButton("Point");
		toolBar.add(tglbtnPoint);
		group.add(tglbtnPoint);

		tglbtnLine = new JToggleButton("Line");
		toolBar.add(tglbtnLine);
		group.add(tglbtnLine);

		tglbtnRectangle = new JToggleButton("Rectangle");
		toolBar.add(tglbtnRectangle);
		group.add(tglbtnRectangle);

		tglbtnCircle = new JToggleButton("Circle");
		toolBar.add(tglbtnCircle);
		group.add(tglbtnCircle);

		tglbtnDonut = new JToggleButton("Donut");
		toolBar.add(tglbtnDonut);
		group.add(tglbtnDonut);

		tglbtnHexagon = new JToggleButton("Hexagon");
		toolBar.add(tglbtnHexagon);
		group.add(tglbtnHexagon);

		tglbtnModify = new JToggleButton("Modify");
		tglbtnModify.setVisible(false);
		toolBar.add(tglbtnModify);
		group.add(tglbtnModify);
		tglbtnModify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				controller.modify();

			}
		});

		tglbtnDelete = new JToggleButton("Delete");
		tglbtnDelete.setVisible(false);

		tglbtnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				controller.delete();
			}
		});
		toolBar.add(tglbtnDelete);
		group.add(tglbtnDelete);
		
		btnToBack = new JButton("To back");
		btnToBack.setVisible(false);
		btnToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.toBack();
			}
		});
		toolBar.add(btnToBack);
		
		btnToFront = new JButton("To front");
		btnToFront.setVisible(false);
		btnToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.toFront();
			}
		});
		toolBar.add(btnToFront);
		
		btnBringToBack = new JButton("Bring to back");
		btnBringToBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToBack();
			}
		});
		toolBar.add(btnBringToBack);
		btnBringToBack.setVisible(false);
		
		btnBringToFront = new JButton("Bring to front");
		btnBringToFront.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.bringToFront();
			}
		});
		toolBar.add(btnBringToFront);
		btnBringToFront.setVisible(false);

		view.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				controller.thisMouseClicked(e);

			}
		});
		getContentPane().add(view, BorderLayout.CENTER);
		
		toolBarSide = new JToolBar();
		toolBarSide.setOrientation(SwingConstants.VERTICAL);
		getContentPane().add(toolBarSide, BorderLayout.WEST);
		btnOutterColor = new JButton("           ");
		btnOutterColor.setToolTipText("Outer color");
		btnOutterColor.setBackground(Color.BLACK);
		btnOutterColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Color color = JColorChooser.showDialog(null, "Select a color", btnOutterColor.getBackground());
				btnOutterColor.setBackground(color);
			}
		});
		toolBarSide.add(btnOutterColor);

		btnInnerColor = new JButton("           ");
		btnInnerColor.setToolTipText("Inner Color");
		btnInnerColor.setBackground(Color.WHITE);

		btnInnerColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Color color = JColorChooser.showDialog(null, "Select a color", btnInnerColor.getBackground());
				btnInnerColor.setBackground(color);
			}
		});
		toolBarSide.add(btnInnerColor);
		
		btnUndo = new JButton("Undo");
		btnUndo.setVisible(false);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
			}
		});
		toolBarSide.add(btnUndo);
		
		btnRedo = new JButton("Redo");
		btnRedo.setVisible(false);
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.redo();
			}
		});
		toolBarSide.add(btnRedo);
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.SOUTH);
		
		list = new JList<String>();
		list.setModel(defaultListModel);

		scrollPane.setViewportView(list);

	}

	public JToggleButton getTglbtnDelete() {
		return tglbtnDelete;
	}

	

	public JButton getBtnInnerColor() {
		return btnInnerColor;
	}

	public JButton getBtnOutterColor() {
		return btnOutterColor;
	}

	public JToggleButton getTglbtnModify() {
		return tglbtnModify;
	}

	public JToggleButton getTglbtnHexagon() {
		return tglbtnHexagon;
	}

	public JToggleButton getTglbtnSelection() {
		return tglbtnSelection;
	}

	public JToggleButton getTglbtnPoint() {
		return tglbtnPoint;
	}

	public JToggleButton getTglbtnLine() {
		return tglbtnLine;
	}

	public JToggleButton getTglbtnRectangle() {
		return tglbtnRectangle;
	}

	public JToggleButton getTglbtnCircle() {
		return tglbtnCircle;
	}

	public JToggleButton getTglbtnDonut() {
		return tglbtnDonut;
	}

	public JButton getBtnToBack() {
		return btnToBack;
	}

	public void setBtnToBack(JButton btnToBack) {
		this.btnToBack = btnToBack;
	}

	public JButton getBtnToFront() {
		return btnToFront;
	}

	public void setBtnToFront(JButton btnToFront) {
		this.btnToFront = btnToFront;
	}

	public JButton getBtnBringToBack() {
		return btnBringToBack;
	}

	public void setBtnBringToBack(JButton btnBringToBack) {
		this.btnBringToBack = btnBringToBack;
	}

	public JButton getBtnBringToFront() {
		return btnBringToFront;
	}

	public void setBtnBringToFront(JButton btnBringToFront) {
		this.btnBringToFront = btnBringToFront;
	}

	public ButtonGroup getGroup() {
		return group;
	}

	public void setGroup(ButtonGroup group) {
		this.group = group;
	}

	public JButton getBtnLoadNext() {
		return btnLoadNext;
	}

	public void setBtnLoadNext(JButton btnLoadNext) {
		this.btnLoadNext = btnLoadNext;
	}

	public JList<String> getList() {
		return list;
	}

	public void setList(JList<String> list) {
		this.list = list;
	}

	
}
