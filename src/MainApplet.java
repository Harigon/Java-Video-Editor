/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJApplet.java
 *
 * Created on 26-Aug-2012, 15:51:21
 */
package src;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import src.dataStore.DataStore;
import src.multiThreading.Event;
import src.multiThreading.ThreadManager;
import src.multiThreading.events.PlayEvent;
import src.multiThreading.threads.DecodingThread;
import src.multiThreading.threads.RenderingThread;
import src.multiThreading.threads.TaskThread;
import src.multiThreading.threads.WorkThread;
import src.renderer.Renderer;
import src.screens.dataDownloaderScreen.DataStoreDownloader;
import src.screens.editorScreen.libraryPanel.chromaKey.ChromaKey;
import src.screens.editorScreen.libraryPanel.chromaKey.ColourProfileManager;
import src.screens.editorScreen.libraryPanel.chromaKey.TriggerManager;
import src.screens.editorScreen.libraryPanel.exportPanel.ExportManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.MediaPanelManager;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.Album;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaItem;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaManager;
import src.screens.editorScreen.libraryPanel.transitionPanel.TransitionManager;
import src.screens.editorScreen.newProjectPanel.NewProject;
import src.screens.editorScreen.previewPanel.PreviewPanelManager;
import src.screens.editorScreen.timeline.TimelineManager;
import src.screens.editorScreen.timeline.track.Track;
import src.screens.editorScreen.timeline.track.TrackObject;
import src.screens.editorScreen.timeline.track.trackItem.TrackItem;
import src.screens.editorScreen.timeline.track.transition.Transition;
import src.screens.editorScreen.timeline.track.transition.transitions.Fade;
import src.screens.editorScreen.timeline.track.transition.transitions.WipeDown;
import src.thirdPartyLibraries.mp3transform.main.org.mp3transform.wav.WavConverter;
import src.screens.editorScreen.libraryPanel.mediaPanel.album.media.MediaVideoItem;

/**
 *
 * @author Harry
 */
public class MainApplet extends javax.swing.JApplet {


	public static MainApplet instance;

	public javax.swing.GroupLayout groupLayout;

	/** Initializes the applet NewJApplet */
	@Override
	public void init() {
		instance = this;
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainApplet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the applet */
		try {
			java.awt.EventQueue.invokeAndWait(new Runnable() {

				public void run() {
					//client client = new client();


/*
					String in = "C:/Users/Harry/Music/music/Fracture Design - Emotions.mp3";
					String out = "output.wav";
					try {
						WavConverter.convert(in, out);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
////
					preInit();

					getContentPane().setBackground(new Color(0, 0, 0));	
					
					
					/*
					 * Run this process on a separate thread, to stop it freezing the GUI.
					 */
					new Thread() {
						public void run() {
							
							DataStoreDownloader.downloadRequiredData();
								
							
							//instance.getContentPane().removeAll();
							//instance.removeAll();
							
							
							MainApplet.getInstance().dataDownloader_mainPanel.setVisible(false);

							
							initComponents();
							
							//MainApplet.getInstance().dataDownloader_mainPanel.setVisible(false);
							//MainApplet.getInstance().getjPanel15().setVisible(true);
						
							
							MainApplet.getInstance().dataDownloader_mainPanel.setVisible(false);
							MainApplet.getInstance().getjPanel32().setVisible(false);
							MainApplet.getInstance().getjPanel16().setVisible(true);
			
							TaskThread.editorRunning = true;

							Project.setupProject(NewProject.dimensions[6][0], NewProject.dimensions[6][1], NewProject.frameRates[0]);
							
							
							 chromaKey.setVisible(false);
							
							getContentPane().setBackground(new Color(0, 0, 0));	
					//initComponents();
					//getContentPane().setBackground(new Color(136, 136, 136));
					//client.go();
					// client.startUp();
//
					events = new ArrayList<Event>();
					eventsToAdd = new ArrayList<Event>();
					eventsToRemove = new ArrayList<Event>();
					
					registerEvent(new PlayEvent());
					//
					ThreadManager.runTaskThread();
					
					Renderer.initRenderer();
					ThreadManager.runRenderingThread();
					ThreadManager.runDecodingThread();
					//WorkThread.run();
					
					
					
					//src.screens.editorScreen.timeline.TimelineManager.addTextTrack();
					src.screens.editorScreen.timeline.TimelineManager.addVideoTrack();
					src.screens.editorScreen.timeline.TimelineManager.addAudioTrack();
					
					Album album = new Album("Default Album");
					MediaManager.selectedAlbum = 0;
					MediaPanelManager.albums.add(album);
					MainApplet.instance.refreshAlbumNameList();
					
					//MediaManager.addCinematic(album, "");
					
					//ChromaKey.refresh();
					
					////MediaManager.addImage("C:/Users/Public/Pictures/Sample Pictures/Dock.jpg");
					//MediaManager.addImage("C:/Users/Public/Pictures/Sample Pictures/Autumn Leaves.jpg");

					//MediaManager.addImage("C:/Users/Harry/Pictures/1.png");
					//MediaManager.addImage("C:/Users/Harry/Pictures/2.png");
					

					
					ExportManager.updateVideoSizePanel();
					NewProject.updateVideoSizePanel();
					PreviewPanelManager.refreshViewModeButtons();
					
					/*TrackObject trackObject = new WipeDown(src.project.timeline.Timeline.tracks.get(0).trackItems.get(0), src.project.timeline.Timeline.tracks.get(0).trackItems.get(1));
					
					Transition transition = (Transition) trackObject;
					
					transition.duration = 50;
					src.project.timeline.Timeline.tracks.get(0).transitions.add(transition);*/
					
					
					//NewJApplet.instance.getTrackManagerPanel1().setLayout(groupLayout);

					RenderingThread.turnedOn = true;
					DecodingThread.turnedOn = true;
					
					refreshTransitionCatagoryList();
					jList1.setSelectedIndex(0);
					
					
						}


					}.start();
					

					
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void preInit(){
		
		 jLabel23 = new javax.swing.JLabel();
		 dataDownloader_mainPanel = new javax.swing.JPanel();
	     jProgressBar2 = new javax.swing.JProgressBar();
	     jLabel22 = new javax.swing.JLabel();
	     
	     
		 dataDownloader_mainPanel.setBackground(new java.awt.Color(0, 0, 0));

	        jProgressBar2.setValue(0);
	        jProgressBar2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
	        jProgressBar2.setName(""); // NOI18N

	        jLabel22.setBackground(new java.awt.Color(204, 204, 204));
	        jLabel22.setFont(new java.awt.Font("Segoe UI Light", 2, 48));
	        jLabel22.setForeground(new java.awt.Color(204, 204, 204));
	        jLabel22.setText("Downloading Required Files...");

	        jLabel23.setBackground(new java.awt.Color(204, 204, 204));
	        jLabel23.setFont(new java.awt.Font("Rockwell", 2, 48));
	        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
	        jLabel23.setText("0%");

	        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(dataDownloader_mainPanel);
	        dataDownloader_mainPanel.setLayout(jPanel29Layout);
	        jPanel29Layout.setHorizontalGroup(
	            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel29Layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 2642, Short.MAX_VALUE)
	                    .addGroup(jPanel29Layout.createSequentialGroup()
	                        .addComponent(jLabel22)
	                        .addGap(18, 18, 18)
	                        .addComponent(jLabel23)))
	                .addContainerGap())
	        );
	        jPanel29Layout.setVerticalGroup(
	            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel29Layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(jLabel22)
	                    .addComponent(jLabel23))
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1342, Short.MAX_VALUE)
	                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
	                .addGap(33, 33, 33))
	        );
	        
	        
	        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
	        getContentPane().setLayout(layout);
	        layout.setHorizontalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                .addComponent(dataDownloader_mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );
	        layout.setVerticalGroup(
	            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                .addComponent(dataDownloader_mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );
	}
	

	/** This method is called from within the init() method to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel16 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        customRealButton2 = new src.CustomRealButton();
        customRealButton4 = new src.CustomRealButton();
        customRealButton5 = new src.CustomRealButton();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        mediaPanel1 = new src.MediaPanel();
        customRealButton3 = new src.CustomRealButton();
        jComboBox3 = new javax.swing.JComboBox();
        jTextField3 = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jSplitPane5 = new javax.swing.JSplitPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        transitionMediaPanel1 = new src.TransitionMediaPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        jPanel17 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel19 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jSlider3 = new javax.swing.JSlider();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jTextField2 = new javax.swing.JTextField();
        jRadioButton3 = new javax.swing.JRadioButton();
        jComboBox2 = new javax.swing.JComboBox();
        jRadioButton4 = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        customRealButton1 = new src.CustomRealButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        imagePanel1 = new src.ImagePanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        customButton1 = new src.CustomButton();
        customButton2 = new src.CustomButton();
        jLabel1 = new javax.swing.JLabel();
        videoSlider1 = new src.VideoSlider();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSplitPane4 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        trackManagerPanel1 = new src.TrackManagerPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        timeline2 = new src.Timeline();
        jPanel24 = new javax.swing.JPanel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel25 = new javax.swing.JPanel();
        customRealButton6 = new src.CustomRealButton();
        jLabel10 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel26 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jPanel27 = new javax.swing.JPanel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();
        jComboBox8 = new javax.swing.JComboBox();
        jTextField7 = new javax.swing.JTextField();
        customRealButton7 = new src.CustomRealButton();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jPanel28 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        chromaKey = new javax.swing.JInternalFrame();
        jPanel36 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        customRealButton10 = new src.CustomRealButton();
        jComboBox11 = new javax.swing.JComboBox();
        jPanel35 = new javax.swing.JPanel();
        jTextField8 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox();
        jScrollPane10 = new javax.swing.JScrollPane();
        customRealButton11 = new src.CustomRealButton();
        customRealButton12 = new src.CustomRealButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        chromaKeyFrame1 = new src.ChromaKeyFrame();
        jPanel29 = new javax.swing.JPanel();
        customRealButton13 = new src.CustomRealButton();
        jComboBox13 = new javax.swing.JComboBox();
        jPanel37 = new javax.swing.JPanel();
        jTextField9 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jComboBox14 = new javax.swing.JComboBox();
        customRealButton14 = new src.CustomRealButton();
        jLabel33 = new javax.swing.JLabel();
        customRealButton15 = new src.CustomRealButton();
        customRealButton16 = new src.CustomRealButton();
        jLabel34 = new javax.swing.JLabel();
        jCheckBox6 = new javax.swing.JCheckBox();
        customRealButton17 = new src.CustomRealButton();
        customRealButton18 = new src.CustomRealButton();
        customRealButton19 = new src.CustomRealButton();
        customRealButton20 = new src.CustomRealButton();
        jLabel35 = new javax.swing.JLabel();
        customRealButton21 = new src.CustomRealButton();
        customRealButton22 = new src.CustomRealButton();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jPanel15 = new javax.swing.JPanel();
        introScreen2 = new src.IntroScreen();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel32 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        customRealButton8 = new src.CustomRealButton();
        customRealButton9 = new src.CustomRealButton();
        jLabel24 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jComboBox9 = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        dataDownloader_mainPanel = new javax.swing.JPanel();
        jProgressBar2 = new javax.swing.JProgressBar();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setBackground(new java.awt.Color(0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLayeredPane1.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                jLayeredPane1AncestorResized(evt);
            }
        });

        jSplitPane2.setBackground(new java.awt.Color(136, 136, 136));
        jSplitPane2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jSplitPane2.setDividerLocation(350);
        jSplitPane2.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {

                    @Override
                    public void paint(Graphics g) {

                        g.setColor(new java.awt.Color(136, 136, 136));
                        g.fillRect(0, 0, getSize().width, getSize().height);

                        g.setColor(new java.awt.Color(171, 171, 171));
                        g.fillRect(0, 0, getSize().width, 1);

                        g.setColor(new java.awt.Color(105, 105, 105));
                        g.fillRect(0, getSize().height-1, getSize().width, 1);
                        super.paint(g);
                    }
                };
            }
        });
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jSplitPane2KeyPressed(evt);
            }
        });

        jSplitPane1.setBackground(new java.awt.Color(136, 136, 136));
        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(620);
        jSplitPane1.setForeground(new java.awt.Color(255, 51, 51));
        jSplitPane1.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {

                    @Override
                    public void paint(Graphics g) {

                        g.setColor(new java.awt.Color(136, 136, 136));
                        g.fillRect(0, 0, getSize().width, getSize().height);

                        g.setColor(new java.awt.Color(171, 171, 171));
                        g.fillRect(0, 0, 1, getSize().height);

                        g.setColor(new java.awt.Color(105, 105, 105));
                        g.fillRect(getSize().width-1, 0, 1, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
        jSplitPane1.setResizeWeight(1.0);

        jPanel2.setBackground(new java.awt.Color(136, 136, 136));
        jPanel2.setMinimumSize(new java.awt.Dimension(456, 0));

        jTabbedPane1.setBackground(new java.awt.Color(136, 136, 136));

        jPanel5.setBackground(new java.awt.Color(136, 136, 136));

        jPanel21.setBackground(new java.awt.Color(102, 102, 102));
        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(171, 171, 171)));

        customRealButton2.setText("Import Media");
        customRealButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton2MouseReleased(evt);
            }
        });
        customRealButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton2ActionPerformed(evt);
            }
        });

        customRealButton4.setText("Import Folder");
        customRealButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton4MouseReleased(evt);
            }
        });
        customRealButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton4ActionPerformed(evt);
            }
        });

        customRealButton5.setText("Create New Album");
        customRealButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton5MouseReleased(evt);
            }
        });
        customRealButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customRealButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customRealButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customRealButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(6, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customRealButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
                .addComponent(customRealButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        customRealButton2.setIcon(DataStore.getImageIcon(9));
        customRealButton2.setOffset(0);
        customRealButton2.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton2.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton2.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton2.setBorder(null);
        customRealButton4.setIcon(DataStore.getImageIcon(9));
        customRealButton4.setOffset(0);
        customRealButton4.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton4.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton4.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton4.setBorder(null);
        customRealButton5.setIcon(DataStore.getImageIcon(9));
        customRealButton5.setOffset(-3);
        customRealButton5.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton5.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton5.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton5.setBorder(null);

        jPanel22.setBackground(new java.awt.Color(102, 102, 102));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(171, 171, 171)));

        jScrollPane8.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        mediaPanel1.setBackground(new java.awt.Color(102, 102, 102));
        mediaPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mediaPanel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                mediaPanel1MouseReleased(evt);
            }
        });
        mediaPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                mediaPanel1MouseMoved(evt);
            }
        });

        javax.swing.GroupLayout mediaPanel1Layout = new javax.swing.GroupLayout(mediaPanel1);
        mediaPanel1.setLayout(mediaPanel1Layout);
        mediaPanel1Layout.setHorizontalGroup(
            mediaPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 326, Short.MAX_VALUE)
        );
        mediaPanel1Layout.setVerticalGroup(
            mediaPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 257, Short.MAX_VALUE)
        );

        jScrollPane8.setViewportView(mediaPanel1);

        customRealButton3.setText("Add All To Timeline");
        customRealButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton3MouseReleased(evt);
            }
        });
        customRealButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton3ActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jTextField3.setText("jTextField3");
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(206, Short.MAX_VALUE)
                .addComponent(customRealButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addComponent(jComboBox3, 0, 208, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11))
        );

        customRealButton3.setIcon(DataStore.getImageIcon(9));
        customRealButton3.setOffset(5);
        customRealButton3.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton3.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton3.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton3.setBorder(null);

        jPanel23.setBackground(new java.awt.Color(102, 102, 102));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(171, 171, 171)));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 131, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Media", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 615, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Text", jPanel6);

        jPanel4.setBackground(new java.awt.Color(98, 98, 98));

        jSplitPane3.setBackground(new java.awt.Color(98, 98, 98));
        jSplitPane3.setDividerLocation(117);
        jSplitPane3.setEnabled(false);
        jSplitPane3.setPreferredSize(new java.awt.Dimension(855, 104));

        jSplitPane5.setBackground(new java.awt.Color(98, 98, 98));
        jSplitPane5.setDividerLocation(220);
        jSplitPane5.setResizeWeight(1.0);
        jSplitPane5.setEnabled(false);

        jScrollPane4.setBackground(new java.awt.Color(98, 98, 98));
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel11.setBackground(new java.awt.Color(98, 98, 98));
        jPanel11.setMinimumSize(new java.awt.Dimension(297, 100));

        jPanel12.setBackground(new java.awt.Color(121, 121, 121));

        jPanel13.setBackground(new java.awt.Color(144, 144, 144));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Fade");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
        );

        jLabel3.setText("Name:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(121, 121, 121));

        jLabel5.setText("Duration:");

        jTextField1.setBackground(new java.awt.Color(204, 204, 204));
        jTextField1.setText("jTextField1");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(934, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(467, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel11);

        jSplitPane5.setRightComponent(jScrollPane4);

        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel10.setBackground(new java.awt.Color(98, 98, 98));
        jPanel10.setEnabled(false);

        transitionMediaPanel1.setBackground(new java.awt.Color(98, 98, 98));
        transitionMediaPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                transitionMediaPanel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                transitionMediaPanel1MouseReleased(evt);
            }
        });
        transitionMediaPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                transitionMediaPanel1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                transitionMediaPanel1MouseMoved(evt);
            }
        });

        javax.swing.GroupLayout transitionMediaPanel1Layout = new javax.swing.GroupLayout(transitionMediaPanel1);
        transitionMediaPanel1.setLayout(transitionMediaPanel1Layout);
        transitionMediaPanel1Layout.setHorizontalGroup(
            transitionMediaPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 217, Short.MAX_VALUE)
        );
        transitionMediaPanel1Layout.setVerticalGroup(
            transitionMediaPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 597, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(transitionMediaPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(transitionMediaPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPane5.setViewportView(jPanel10);

        jSplitPane5.setLeftComponent(jScrollPane5);

        jSplitPane3.setRightComponent(jSplitPane5);
        jSplitPane5.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {

                    @Override
                    public void paint(Graphics g) {

                        g.setColor(new java.awt.Color(136, 136, 136));
                        g.fillRect(0, 0, getSize().width, getSize().height);

                        g.setColor(new java.awt.Color(171, 171, 171));
                        g.fillRect(0, 0, 1, getSize().height);

                        g.setColor(new java.awt.Color(105, 105, 105));
                        g.fillRect(getSize().width-1, 0, 1, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });

        jScrollPane6.setBorder(null);
        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane6.setMinimumSize(new java.awt.Dimension(117, 5));

        jPanel9.setBackground(new java.awt.Color(98, 98, 98));

        jList1.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane7.setViewportView(jList1);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
        );

        jScrollPane6.setViewportView(jPanel9);

        jSplitPane3.setLeftComponent(jScrollPane6);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
        );

        jSplitPane3.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {

                    @Override
                    public void paint(Graphics g) {

                        g.setColor(new java.awt.Color(136, 136, 136));
                        g.fillRect(0, 0, getSize().width, getSize().height);

                        g.setColor(new java.awt.Color(171, 171, 171));
                        g.fillRect(0, 0, 1, getSize().height);

                        g.setColor(new java.awt.Color(105, 105, 105));
                        g.fillRect(getSize().width-1, 0, 1, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });

        jTabbedPane1.addTab("Transitions", jPanel4);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 615, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 320, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Effects", jPanel8);

        jPanel3.setBackground(new java.awt.Color(136, 136, 136));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MOV - QuickTime Movie", "AVI - Audio Video Interleave video file", "GIF - animation file" }));

        jLabel4.setText("Export format: ");

        jCheckBox1.setBackground(new java.awt.Color(136, 136, 136));
        jCheckBox1.setText("Export loop-region only");

        jCheckBox2.setBackground(new java.awt.Color(136, 136, 136));
        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Preview export progress");

        jPanel17.setBackground(new java.awt.Color(153, 153, 153));
        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabbedPane3.setBackground(new java.awt.Color(102, 102, 102));

        jPanel19.setBackground(new java.awt.Color(102, 102, 102));
        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setText("Video codec: ");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "JPEG", "PNG" }));

        jLabel13.setText("Compression: ");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jSlider3, 0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Video", jPanel19);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
        );

        jPanel17.setBounds(0, 0, 240, 120);
        jLayeredPane2.add(jPanel17, javax.swing.JLayeredPane.DEFAULT_LAYER);

        jLabel8.setText("Format settings:");

        jLabel9.setText("Video size: ");

        jPanel20.setBackground(new java.awt.Color(153, 153, 153));
        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel18.setBackground(new java.awt.Color(102, 102, 102));

        jRadioButton1.setBackground(new java.awt.Color(102, 102, 102));
        jRadioButton1.setText("Project dimensions");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jTextField2.setText("1080 x 720");
        jTextField2.setEnabled(false);

        jRadioButton3.setBackground(new java.awt.Color(102, 102, 102));
        jRadioButton3.setText("Preset dimensions");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1920 x 1080", "1680 x 1050", "1600 x 1024", "1400 x 1050", "1280 x 1024", "1366 x 768", "1280 x 720", "1024 x 768", "800 x 600", "640 x 480" }));

        jRadioButton4.setBackground(new java.awt.Color(102, 102, 102));
        jRadioButton4.setText("Custom size");
        jRadioButton4.setEnabled(false);
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jLabel6.setText("Width: ");
        jLabel6.setEnabled(false);

        jTextField4.setEnabled(false);

        jLabel7.setText("Height: ");
        jLabel7.setEnabled(false);

        jTextField5.setEnabled(false);

        jCheckBox3.setBackground(new java.awt.Color(102, 102, 102));
        jCheckBox3.setText("Maintain aspect ratio");
        jCheckBox3.setEnabled(false);
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jRadioButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRadioButton4)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField5)
                            .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jCheckBox3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton3)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        customRealButton1.setText("Start");
        customRealButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton1MouseReleased(evt);
            }
        });
        customRealButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox2)
                            .addComponent(jCheckBox1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
                        .addComponent(customRealButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4))
                            .addComponent(jLabel9)
                            .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(customRealButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addGap(2, 2, 2)
                                .addComponent(jCheckBox2)))))
                .addContainerGap())
        );

        customRealButton1.setIcon(DataStore.getImageIcon(9));
        customRealButton1.setOffset(0);
        customRealButton1.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton1.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton1.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton1.setBorder(null);

        jTabbedPane1.addTab("         Export          ", jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(jPanel2);

        jPanel1.setBackground(new java.awt.Color(136, 136, 136));
        jPanel1.setMinimumSize(new java.awt.Dimension(466, 350));

        jPanel7.setBackground(new java.awt.Color(136, 136, 136));
        jPanel7.setMaximumSize(new java.awt.Dimension(466, 298));
        jPanel7.setMinimumSize(new java.awt.Dimension(466, 298));

        imagePanel1.setBackground(new java.awt.Color(136, 136, 136));
        imagePanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imagePanel1MouseClicked(evt);
            }
        });
        imagePanel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                imagePanel1KeyPresd(evt);
            }
        });

        javax.swing.GroupLayout imagePanel1Layout = new javax.swing.GroupLayout(imagePanel1);
        imagePanel1.setLayout(imagePanel1Layout);
        imagePanel1Layout.setHorizontalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 656, Short.MAX_VALUE)
        );
        imagePanel1Layout.setVerticalGroup(
            imagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );

        jToggleButton1.setDisabledIcon(DataStore.getImageIcon(1));
        jToggleButton1.setIcon(DataStore.getImageIcon(1));

        jToggleButton1.setSelectedIcon(DataStore.getImageIcon(3));
        jToggleButton1.setRolloverEnabled(true); // turn on before rollovers work
        jToggleButton1.setRolloverIcon(DataStore.getImageIcon(2));
        jToggleButton1.setBorder(null);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jButton1.setIcon(DataStore.getImageIcon(4));

        jButton1.setSelectedIcon(DataStore.getImageIcon(6));
        jButton1.setRolloverEnabled(true); // turn on before rollovers work
        jButton1.setRolloverIcon(DataStore.getImageIcon(5));
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(DataStore.getImageIcon(11));

        jButton2.setSelectedIcon(DataStore.getImageIcon(12));
        jButton2.setRolloverEnabled(true); // turn on before rollovers work
        jButton2.setRolloverIcon(DataStore.getImageIcon(10));
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(DataStore.getImageIcon(13));

        jButton3.setSelectedIcon(DataStore.getImageIcon(15));
        jButton3.setRolloverEnabled(true); // turn on before rollovers work
        jButton3.setRolloverIcon(DataStore.getImageIcon(14));
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        customButton1.setText("Entire Project");
        customButton1.setIcon(DataStore.getImageIcon(9));
        customButton1.setOffset(5);
        customButton1.setSelectedIcon(DataStore.getImageIcon(8));
        customButton1.setRolloverEnabled(true); // turn on before rollovers work
        customButton1.setRolloverIcon(DataStore.getImageIcon(7));
        customButton1.setBorder(null);
        customButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customButton1ActionPerformed(evt);
            }
        });

        customButton2.setText("Selected Media");
        customButton2.setIcon(DataStore.getImageIcon(9));
        customButton2.setOffset(0);
        customButton2.setSelectedIcon(DataStore.getImageIcon(8));
        customButton2.setRolloverEnabled(true); // turn on before rollovers work
        customButton2.setRolloverIcon(DataStore.getImageIcon(7));
        customButton2.setBorder(null);
        customButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Play: ");

        jButton4.setIcon(DataStore.getImageIcon(16));

        jButton4.setSelectedIcon(DataStore.getImageIcon(16));
        jButton4.setRolloverEnabled(true); // turn on before rollovers work
        jButton4.setRolloverIcon(DataStore.getImageIcon(16));
        jButton4.setBorder(null);

        jButton5.setIcon(DataStore.getImageIcon(16));

        jButton5.setSelectedIcon(DataStore.getImageIcon(16));
        jButton5.setRolloverEnabled(true); // turn on before rollovers work
        jButton5.setRolloverIcon(DataStore.getImageIcon(16));
        jButton5.setBorder(null);

        javax.swing.GroupLayout videoSlider1Layout = new javax.swing.GroupLayout(videoSlider1);
        videoSlider1.setLayout(videoSlider1Layout);
        videoSlider1Layout.setHorizontalGroup(
            videoSlider1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(videoSlider1Layout.createSequentialGroup()
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 588, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        videoSlider1Layout.setVerticalGroup(
            videoSlider1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 261, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(videoSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(imagePanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imagePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(videoSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(customButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(customButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jPanel1);

        jSplitPane2.setTopComponent(jSplitPane1);

        jSplitPane4.setBackground(new java.awt.Color(136, 136, 136));
        jSplitPane4.setBorder(null);
        jSplitPane4.setDividerLocation(150);
        jSplitPane4.setEnabled(false);

        jScrollPane2.setBackground(new java.awt.Color(136, 136, 136));
        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                jScrollPane2VetoableChange(evt);
            }
        });

        trackManagerPanel1.setBackground(new java.awt.Color(121, 121, 121));

        javax.swing.GroupLayout trackManagerPanel1Layout = new javax.swing.GroupLayout(trackManagerPanel1);
        trackManagerPanel1.setLayout(trackManagerPanel1Layout);
        trackManagerPanel1Layout.setHorizontalGroup(
            trackManagerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 133, Short.MAX_VALUE)
        );
        trackManagerPanel1Layout.setVerticalGroup(
            trackManagerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1191, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(trackManagerPanel1);

        jSplitPane4.setLeftComponent(jScrollPane2);
        jScrollPane2.getViewport().addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                jScrollPane2Change(e);
            }
        });

        jScrollPane3.setBackground(new java.awt.Color(136, 136, 136));
        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        timeline2.setBackground(new java.awt.Color(121, 121, 121));
        timeline2.setPreferredSize(new java.awt.Dimension(700, 612));
        timeline2.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                timeline2MouseWheelMoved(evt);
            }
        });
        timeline2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                timeline2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                timeline2MouseReleased(evt);
            }
        });
        timeline2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                timeline2MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                timeline2MouseMoved(evt);
            }
        });
        timeline2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                timeline2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                timeline2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout timeline2Layout = new javax.swing.GroupLayout(timeline2);
        timeline2.setLayout(timeline2Layout);
        timeline2Layout.setHorizontalGroup(
            timeline2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1126, Short.MAX_VALUE)
        );
        timeline2Layout.setVerticalGroup(
            timeline2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 612, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(timeline2);

        jSplitPane4.setRightComponent(jScrollPane3);

        jSplitPane2.setRightComponent(jSplitPane4);
        jSplitPane4.setUI(new BasicSplitPaneUI() {
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {

                    @Override
                    public void paint(Graphics g) {

                        g.setColor(new java.awt.Color(136, 136, 136));
                        g.fillRect(0, 0, getSize().width, getSize().height);

                        g.setColor(new java.awt.Color(171, 171, 171));
                        g.fillRect(0, 0, 1, getSize().height);

                        g.setColor(new java.awt.Color(105, 105, 105));
                        g.fillRect(getSize().width-1, 0, 1, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1285, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 907, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel16.setBounds(0, 0, 1285, 918);
        jLayeredPane1.add(jPanel16, javax.swing.JLayeredPane.DEFAULT_LAYER);
        //jPanel16.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
        //jPanel16.repaint();
        jPanel16.hide();

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );

        jPanel24.setBounds(0, 0, 260, 138);
        jLayeredPane1.add(jPanel24, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jPanel24.hide();

        jInternalFrame1.setBackground(new java.awt.Color(136, 136, 136));
        jInternalFrame1.setTitle("Multi-adder");
        jInternalFrame1.setVisible(true);

        jPanel25.setBackground(new java.awt.Color(136, 136, 136));

        customRealButton6.setText("Add All");
        customRealButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton6MouseReleased(evt);
            }
        });
        customRealButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton6ActionPerformed(evt);
            }
        });

        jLabel10.setText("Total items: ");

        jTabbedPane2.setBackground(new java.awt.Color(102, 102, 102));

        jPanel26.setBackground(new java.awt.Color(102, 102, 102));
        jPanel26.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setText("Image duration: ");

        jLabel14.setText("Destination track: ");

        jLabel15.setText("Position on track:");

        jTextField6.setText("jTextField6");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "End of track" }));

        jCheckBox4.setBackground(new java.awt.Color(102, 102, 102));
        jCheckBox4.setSelected(true);
        jCheckBox4.setText("Add image items");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox4)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField6)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(259, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Image settings", jPanel26);

        jPanel27.setBackground(new java.awt.Color(102, 102, 102));
        jPanel27.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jCheckBox5.setBackground(new java.awt.Color(102, 102, 102));
        jCheckBox5.setSelected(true);
        jCheckBox5.setText("Add audio items");
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        jLabel16.setText("Image duration: ");

        jLabel17.setText("Destination track: ");

        jLabel18.setText("Position on track:");

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "End of track" }));

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextField7.setText("jTextField6");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox5)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox7, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField7)
                            .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jCheckBox5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(259, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Audio settings", jPanel27);

        customRealButton7.setText("Cancel");
        customRealButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton7MouseReleased(evt);
            }
        });
        customRealButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                        .addComponent(customRealButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customRealButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(customRealButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customRealButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        customRealButton6.setIcon(DataStore.getImageIcon(9));
        customRealButton6.setOffset(0);
        customRealButton6.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton6.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton6.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton6.setBorder(null);
        customRealButton7.setIcon(DataStore.getImageIcon(9));
        customRealButton7.setOffset(0);
        customRealButton7.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton7.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton7.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton7.setBorder(null);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jInternalFrame1.setBounds(0, 0, 337, 499);
        jLayeredPane1.add(jInternalFrame1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jInternalFrame1.setVisible(false);

        jInternalFrame2.setBackground(new java.awt.Color(136, 136, 136));
        jInternalFrame2.setClosable(true);
        jInternalFrame2.setTitle("Renderer");
        jInternalFrame2.setVisible(true);

        jPanel28.setBackground(new java.awt.Color(136, 136, 136));

        jLabel19.setText("Frame 0 / ?");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 36));
        jLabel20.setText("0%");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 18));
        jLabel21.setText("Rendering video...");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel21))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(234, 234, 234)
                        .addComponent(jLabel19))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addGap(237, 237, 237)
                        .addComponent(jLabel20)))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addContainerGap(119, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jInternalFrame2Layout = new javax.swing.GroupLayout(jInternalFrame2.getContentPane());
        jInternalFrame2.getContentPane().setLayout(jInternalFrame2Layout);
        jInternalFrame2Layout.setHorizontalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jInternalFrame2Layout.setVerticalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jInternalFrame2.setBounds(0, 0, 579, 289);
        jLayeredPane1.add(jInternalFrame2, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jInternalFrame2.setVisible(false);

        chromaKey.setBackground(new java.awt.Color(136, 136, 136));
        chromaKey.setClosable(true);
        chromaKey.setResizable(true);
        chromaKey.setTitle("Chroma Keyer");
        chromaKey.setVisible(true);

        jPanel34.setBackground(new java.awt.Color(102, 102, 102));

        customRealButton10.setText("New Colour Profile");
        customRealButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton10MouseReleased(evt);
            }
        });
        customRealButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton10ActionPerformed(evt);
            }
        });

        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox11ActionPerformed(evt);
            }
        });

        jPanel35.setBackground(new java.awt.Color(87, 86, 86));

        jTextField8.setText("jTextField8");
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField8KeyReleased(evt);
            }
        });

        jLabel28.setText("Profile name: ");

        jLabel29.setText("Colour lists: ");

        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Filtered Colours", "Allowed Colours" }));
        jComboBox12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox12ActionPerformed(evt);
            }
        });

        customRealButton11.setText("Remove Profile");
        customRealButton11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton11MouseReleased(evt);
            }
        });
        customRealButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton11ActionPerformed(evt);
            }
        });

        customRealButton12.setText("Add Colour");
        customRealButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton12MouseReleased(evt);
            }
        });
        customRealButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(jLabel28)
                    .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addComponent(jLabel29)
                    .addComponent(jComboBox12, 0, 118, Short.MAX_VALUE)
                    .addComponent(customRealButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customRealButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(customRealButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        customRealButton11.setIcon(DataStore.getImageIcon(9));
        customRealButton11.setOffset(0);
        customRealButton11.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton11.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton11.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton11.setBorder(null);
        customRealButton12.setIcon(DataStore.getImageIcon(9));
        customRealButton12.setOffset(0);
        customRealButton12.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton12.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton12.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton12.setBorder(null);

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox11, 0, 138, Short.MAX_VALUE)
                            .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                        .addComponent(customRealButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customRealButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(66, 66, 66))
        );

        customRealButton10.setIcon(DataStore.getImageIcon(9));
        customRealButton10.setOffset(0);
        customRealButton10.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton10.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton10.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton10.setBorder(null);

        chromaKeyFrame1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                chromaKeyFrame1MouseReleased(evt);
            }
        });
        chromaKeyFrame1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                chromaKeyFrame1MouseMoved(evt);
            }
        });

        javax.swing.GroupLayout chromaKeyFrame1Layout = new javax.swing.GroupLayout(chromaKeyFrame1);
        chromaKeyFrame1.setLayout(chromaKeyFrame1Layout);
        chromaKeyFrame1Layout.setHorizontalGroup(
            chromaKeyFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
        );
        chromaKeyFrame1Layout.setVerticalGroup(
            chromaKeyFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 587, Short.MAX_VALUE)
        );

        jScrollPane9.setViewportView(chromaKeyFrame1);

        jPanel29.setBackground(new java.awt.Color(102, 102, 102));

        customRealButton13.setText("New Trigger");
        customRealButton13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton13MouseReleased(evt);
            }
        });
        customRealButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton13ActionPerformed(evt);
            }
        });

        jComboBox13.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox13ActionPerformed(evt);
            }
        });

        jPanel37.setBackground(new java.awt.Color(87, 86, 86));

        jTextField9.setText("jTextField8");
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField9KeyReleased(evt);
            }
        });

        jLabel31.setText("Trigger name: ");

        jLabel32.setText("Colour profile: ");

        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Filtered Colours", "Allowed Colours" }));
        jComboBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox14ActionPerformed(evt);
            }
        });

        customRealButton14.setText("Remove Trigger");
        customRealButton14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton14MouseReleased(evt);
            }
        });
        customRealButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton14ActionPerformed(evt);
            }
        });

        jLabel33.setText("Trigger position: 30, 50");

        customRealButton15.setText("Set Trigger X");
        customRealButton15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton15MouseReleased(evt);
            }
        });
        customRealButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton15ActionPerformed(evt);
            }
        });

        customRealButton16.setText("Set Trigger Y");
        customRealButton16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton16MouseReleased(evt);
            }
        });
        customRealButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton16ActionPerformed(evt);
            }
        });

        jLabel34.setText("Trigger min-range: 30, 50");

        jCheckBox6.setBackground(new java.awt.Color(87, 86, 86));
        jCheckBox6.setText("Position fill-bomb");
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        customRealButton17.setText("Set Boundary-Min X");
        customRealButton17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton17MouseReleased(evt);
            }
        });
        customRealButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton17ActionPerformed(evt);
            }
        });

        customRealButton18.setText("Set Boundary-Min Y");
        customRealButton18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton18MouseReleased(evt);
            }
        });
        customRealButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton18ActionPerformed(evt);
            }
        });

        customRealButton19.setText("Set Boundary-Max X");
        customRealButton19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton19MouseReleased(evt);
            }
        });
        customRealButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton19ActionPerformed(evt);
            }
        });

        customRealButton20.setText("Set Boundary-Max Y");
        customRealButton20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton20MouseReleased(evt);
            }
        });
        customRealButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton20ActionPerformed(evt);
            }
        });

        jLabel35.setText("Trigger max-range: 30, 50");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customRealButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31)
                            .addComponent(jTextField9, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                            .addComponent(jLabel32)
                            .addComponent(jComboBox14, 0, 134, Short.MAX_VALUE)))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(customRealButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customRealButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jCheckBox6)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel34))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customRealButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customRealButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(customRealButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customRealButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(customRealButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        customRealButton14.setIcon(DataStore.getImageIcon(9));
        customRealButton14.setOffset(0);
        customRealButton14.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton14.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton14.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton14.setBorder(null);
        customRealButton15.setIcon(DataStore.getImageIcon(9));
        customRealButton15.setOffset(0);
        customRealButton15.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton15.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton15.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton15.setBorder(null);
        customRealButton16.setIcon(DataStore.getImageIcon(9));
        customRealButton16.setOffset(0);
        customRealButton16.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton16.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton16.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton16.setBorder(null);
        customRealButton17.setIcon(DataStore.getImageIcon(9));
        customRealButton17.setOffset(0);
        customRealButton17.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton17.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton17.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton17.setBorder(null);
        customRealButton18.setIcon(DataStore.getImageIcon(9));
        customRealButton18.setOffset(0);
        customRealButton18.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton18.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton18.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton18.setBorder(null);
        customRealButton19.setIcon(DataStore.getImageIcon(9));
        customRealButton19.setOffset(0);
        customRealButton19.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton19.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton19.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton19.setBorder(null);
        customRealButton20.setIcon(DataStore.getImageIcon(9));
        customRealButton20.setOffset(0);
        customRealButton20.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton20.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton20.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton20.setBorder(null);

        customRealButton21.setText("Previous Frame");
        customRealButton21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton21MouseReleased(evt);
            }
        });
        customRealButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton21ActionPerformed(evt);
            }
        });

        customRealButton22.setText("Next Frame");
        customRealButton22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton22MouseReleased(evt);
            }
        });
        customRealButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton22ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(customRealButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customRealButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customRealButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel37, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jComboBox13, javax.swing.GroupLayout.Alignment.LEADING, 0, 154, Short.MAX_VALUE))
                    .addGap(12, 12, 12)))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(customRealButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 534, Short.MAX_VALUE)
                .addComponent(customRealButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customRealButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel29Layout.createSequentialGroup()
                    .addGap(38, 38, 38)
                    .addComponent(jComboBox13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(98, Short.MAX_VALUE)))
        );

        customRealButton13.setIcon(DataStore.getImageIcon(9));
        customRealButton13.setOffset(0);
        customRealButton13.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton13.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton13.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton13.setBorder(null);
        customRealButton21.setIcon(DataStore.getImageIcon(9));
        customRealButton21.setOffset(0);
        customRealButton21.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton21.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton21.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton21.setBorder(null);
        customRealButton22.setIcon(DataStore.getImageIcon(9));
        customRealButton22.setOffset(0);
        customRealButton22.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton22.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton22.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton22.setBorder(null);

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        chromaKey.setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout chromaKeyLayout = new javax.swing.GroupLayout(chromaKey.getContentPane());
        chromaKey.getContentPane().setLayout(chromaKeyLayout);
        chromaKeyLayout.setHorizontalGroup(
            chromaKeyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        chromaKeyLayout.setVerticalGroup(
            chromaKeyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        chromaKey.setBounds(0, 0, 793, 665);
        jLayeredPane1.add(chromaKey, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jInternalFrame2.setVisible(false);

        introScreen2.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout introScreen2Layout = new javax.swing.GroupLayout(introScreen2);
        introScreen2.setLayout(introScreen2Layout);
        introScreen2Layout.setHorizontalGroup(
            introScreen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2662, Short.MAX_VALUE)
        );
        introScreen2Layout.setVerticalGroup(
            introScreen2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1485, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(introScreen2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(introScreen2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel30.setBackground(new java.awt.Color(0, 0, 0));
        jPanel30.setLayout(new java.awt.GridBagLayout());

        jPanel31.setBackground(new java.awt.Color(153, 153, 153));
        jPanel31.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel31.setPreferredSize(new java.awt.Dimension(420, 270));

        jButton6.setBackground(new java.awt.Color(153, 153, 153));
        jButton6.setFont(new java.awt.Font("Tahoma", 0, 36));
        jButton6.setText("New Project");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(153, 153, 153));
        jButton7.setFont(new java.awt.Font("Tahoma", 0, 36));
        jButton7.setText("Open Project");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel30.add(jPanel31, new java.awt.GridBagConstraints());

        jPanel32.setBackground(new java.awt.Color(0, 0, 0));
        jPanel32.setLayout(new java.awt.GridBagLayout());

        jPanel33.setBackground(new java.awt.Color(153, 153, 153));
        jPanel33.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel33.setPreferredSize(new java.awt.Dimension(370, 150));

        customRealButton8.setText("OK");
        customRealButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton8MouseReleased(evt);
            }
        });
        customRealButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton8ActionPerformed(evt);
            }
        });

        customRealButton9.setText("Cancel");
        customRealButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customRealButton9MouseReleased(evt);
            }
        });
        customRealButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customRealButton9ActionPerformed(evt);
            }
        });

        jLabel24.setText("Project name: ");

        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField11KeyReleased(evt);
            }
        });

        jLabel25.setText("*");

        jLabel26.setText("Frame rate: ");

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "29.970 (NTSC)", "25.000 (PAL)", "24.000 (Film)", "23.976 (Theatrical)" }));

        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1920 x 1080 (HDTV)", "1680 x 1050 (WSXGA+)", "1600 x 1024 (WSXGA)", "1400 x 1050 (SXGA+)", "1280 x 1024 (SXGA)", "1366 x 768 (WXGA)", "1280 x 720 (HDTV)", "1024 x 768 (XGA)", "800 x 600 (SVGA)", "640 x 480 (VGA)" }));

        jLabel27.setText("Project Dimensions: ");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(jLabel26)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(customRealButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(customRealButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox10, javax.swing.GroupLayout.Alignment.LEADING, 0, 166, Short.MAX_VALUE)
                            .addComponent(jComboBox9, javax.swing.GroupLayout.Alignment.LEADING, 0, 166, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addGap(129, 129, 129))))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(18, 18, 18)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(customRealButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customRealButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        customRealButton8.setIcon(DataStore.getImageIcon(9));
        customRealButton8.setOffset(0);
        customRealButton8.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton8.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton8.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton8.setBorder(null);
        customRealButton9.setIcon(DataStore.getImageIcon(9));
        customRealButton9.setOffset(0);
        customRealButton9.setSelectedIcon(DataStore.getImageIcon(8));
        customRealButton9.setRolloverEnabled(true); // turn on before rollovers work
        customRealButton9.setRolloverIcon(DataStore.getImageIcon(7));
        customRealButton9.setBorder(null);

        jPanel32.add(jPanel33, new java.awt.GridBagConstraints());

        dataDownloader_mainPanel.setBackground(new java.awt.Color(0, 0, 0));

        jProgressBar2.setValue(50);
        jProgressBar2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jProgressBar2.setName(""); // NOI18N

        jLabel22.setBackground(new java.awt.Color(204, 204, 204));
        jLabel22.setFont(new java.awt.Font("Segoe UI Light", 2, 48));
        jLabel22.setForeground(new java.awt.Color(204, 204, 204));
        jLabel22.setText("Downloading Required Files...");

        jLabel23.setBackground(new java.awt.Color(204, 204, 204));
        jLabel23.setFont(new java.awt.Font("Rockwell", 2, 48));
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("32%");

        javax.swing.GroupLayout dataDownloader_mainPanelLayout = new javax.swing.GroupLayout(dataDownloader_mainPanel);
        dataDownloader_mainPanel.setLayout(dataDownloader_mainPanelLayout);
        dataDownloader_mainPanelLayout.setHorizontalGroup(
            dataDownloader_mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataDownloader_mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataDownloader_mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 2642, Short.MAX_VALUE)
                    .addGroup(dataDownloader_mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel23)))
                .addContainerGap())
        );
        dataDownloader_mainPanelLayout.setVerticalGroup(
            dataDownloader_mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataDownloader_mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataDownloader_mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1342, Short.MAX_VALUE)
                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jMenuBar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jMenuBar1KeyPressed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem1.setText("New Project");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Open Project");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Save Project");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Save Project as");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 2662, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, 2662, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, 2662, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(dataDownloader_mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1485, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, 1485, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, 1485, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(dataDownloader_mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        //jLayeredPane1.moveToFront(introScreen_mainPanel);
        //jPanel15.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
        //jPanel15.repaint();
        jPanel15.setVisible(false);
        //jLayeredPane1.moveToFront(introScreen_mainPanel);
        //jPanel15.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
        //jPanel15.repaint();
        jPanel30.setVisible(false);
        //jLayeredPane1.moveToFront(introScreen_mainPanel);
        //jPanel15.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
        //jPanel15.repaint();
        jPanel32.setVisible(false);
        //jLayeredPane1.moveToFront(introScreen_mainPanel);
        //jPanel15.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
        //jPanel15.repaint();
        dataDownloader_mainPanel.setVisible(true);
    }// </editor-fold>//GEN-END:initComponents

	private void imagePanel1KeyPresd(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_imagePanel1KeyPresd
		System.out.println("YO MOFO");
	}//GEN-LAST:event_imagePanel1KeyPresd

	private void imagePanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagePanel1MouseClicked
		int x = evt.getX();
		int y = evt.getY();
		System.out.println("X: "+x+", Y: "+y);
	}//GEN-LAST:event_imagePanel1MouseClicked

	private void jScrollPane2VetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jScrollPane2VetoableChange
		System.out.println("mother fucker");
	}//GEN-LAST:event_jScrollPane2VetoableChange

	
	public void refreshTransitionCatagoryList(){
    	

    	
    	 jList1.setModel(new javax.swing.AbstractListModel() {
             public int getSize() { 
             	return TransitionManager.categoryNames.length; 
             	}
             public Object getElementAt(int i) {
            	 if(i == -1){
            		 return null;
            	 }
             	return TransitionManager.categoryNames[i]; 
             }
         });

    }
	
	private void timeline2MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_timeline2MouseWheelMoved


		if(evt.getWheelRotation() > 0){
			src.screens.editorScreen.timeline.TimelineManager.framesPerPixel += 0.1; 
		}

		if(evt.getWheelRotation() < 0){
			src.screens.editorScreen.timeline.TimelineManager.framesPerPixel -= 0.1; 
		}


		if(src.screens.editorScreen.timeline.TimelineManager.framesPerPixel < 1){
			src.screens.editorScreen.timeline.TimelineManager.framesPerPixel = 1;
		}


		Timeline.update();


	}//GEN-LAST:event_timeline2MouseWheelMoved

	private void timeline2MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeline2MouseMoved
		int x = evt.getX();
		int y = evt.getY();

		timeline2.setMousePosition(x, y);

		//Timeline.update();
		//System.out.println("x: "+x+", y: "+y);

	}//GEN-LAST:event_timeline2MouseMoved

	private void timeline2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeline2MousePressed
		int x = evt.getX();
		int y = evt.getY();
		timeline2.mousePressed(x, y);
	}//GEN-LAST:event_timeline2MousePressed

	private void timeline2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeline2MouseDragged

		int x = evt.getX();
		int y = evt.getY();
		
		

		timeline2.setMousePosition(x, y);
		//System.out.println(x);
		
		if(timeline2.croppedTrackItem != null){

			int currentEndPosition = timeline2.croppedTrackItem.trackStartPosition+timeline2.croppedTrackItem.mediaDuration;
			
			int newTrackEndPosition = src.screens.editorScreen.timeline.track.TrackManager.pixelPositionToTrackPosition(x);
			
			
			if(timeline2.draggedTrackItem != null){
				
				if(src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(timeline2.draggedTrackItem).trackItems != null){
			
			for(TrackItem trackItem2 : src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(timeline2.draggedTrackItem).trackItems){

				if(trackItem2 == timeline2.croppedTrackItem){
					continue;
				}
				int endingPosition = trackItem2.trackStartPosition+trackItem2.mediaDuration;
				
				int startingPosition = trackItem2.trackStartPosition;

				if(newTrackEndPosition >= startingPosition+1 && currentEndPosition < startingPosition+1){
					newTrackEndPosition = startingPosition+1;
				}

			}
			int newEndPosition = src.screens.editorScreen.timeline.track.TrackManager.pixelPositionToTrackPosition(newTrackEndPosition);
			
			int newDuration = newEndPosition-timeline2.croppedTrackItem.trackStartPosition;
			
			if(newDuration < 1){
				newDuration = 1;
			}
			
			//System.out.println("new duration: "+newDuration+" || "+newEndPosition+"-"+timeline2.croppedTrackItem.trackStartPosition);
			timeline2.croppedTrackItem.mediaDuration = newDuration;
			
			src.screens.editorScreen.timeline.track.transition.TransitionManager.checkItem(timeline2.croppedTrackItem);
			src.screens.editorScreen.timeline.TimelineManager.updateTimelineLength();
		}
			}
		}

		if(timeline2.draggedTrackItem != null && timeline2.croppedTrackItem == null){
			int newPosition = x-timeline2.itemDraggedOffset;



			int newTrackPosition = src.screens.editorScreen.timeline.track.TrackManager.pixelPositionToTrackPosition(newPosition);
			
			int newTrackEndPosition = newTrackPosition+timeline2.draggedTrackItem.mediaDuration;

			//System.out.println("Set position 1: "+newTrackPosition);

			if(newTrackPosition < 0){
				newTrackPosition = 0;
			}




			int currentPosition = timeline2.draggedTrackItem.trackStartPosition;
			
			int currentEndPosition = timeline2.draggedTrackItem.trackStartPosition + timeline2.draggedTrackItem.mediaDuration;


			if(timeline2.draggedStuckToLeft == null){
				for(TrackItem trackItem2 : src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(timeline2.draggedTrackItem).trackItems){

					if(trackItem2 == timeline2.draggedTrackItem){
						continue;
					}
					int endingPosition = trackItem2.trackStartPosition+trackItem2.mediaDuration;

					if((currentPosition > endingPosition+1 && newTrackPosition < endingPosition+1) || currentPosition == endingPosition+1){
						timeline2.draggedStuckToLeft = trackItem2;
						timeline2.draggedStickDelayLeft = 30;
					}

				}
			}
			
			if(timeline2.draggedStuckToRight == null){
				for(TrackItem trackItem2 : src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(timeline2.draggedTrackItem).trackItems){

					if(trackItem2 == timeline2.draggedTrackItem){
						continue;
					}
					int endingPosition = trackItem2.trackStartPosition+trackItem2.mediaDuration;
					
					int startingPosition = trackItem2.trackStartPosition;

					if((currentEndPosition < startingPosition-1 && newTrackEndPosition > startingPosition-1) || currentEndPosition == startingPosition-1){
						timeline2.draggedStuckToRight = trackItem2;
						timeline2.draggedStickDelayRight = 30;
					}

				}
			}
			
			
			if(newTrackPosition < currentPosition){
				timeline2.draggedStickDelayLeft -= timeline2.itemDraggedLastMouseX-x;
			}
			if(newTrackPosition > currentPosition){
				timeline2.draggedStickDelayRight += timeline2.itemDraggedLastMouseX-x;
			}

			if(timeline2.draggedStuckToLeft != null){
				if(newTrackPosition > currentPosition || timeline2.draggedStickDelayLeft <= 0){
					timeline2.draggedStuckToLeft = null;
					timeline2.draggedStickDelayLeft = 0;
				}
			}
			
			if(timeline2.draggedStuckToRight != null){
				if(newTrackPosition < currentPosition || timeline2.draggedStickDelayRight <= 0){
					timeline2.draggedStuckToRight = null;
					timeline2.draggedStickDelayRight = 0;
				}
			}




			if(timeline2.draggedStuckToLeft != null){
				newTrackPosition = (timeline2.draggedStuckToLeft.trackStartPosition+timeline2.draggedStuckToLeft.mediaDuration)+1;
				timeline2.itemDraggedOffset = x-src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(timeline2.draggedTrackItem.trackStartPosition);

			}
			
			if(timeline2.draggedStuckToRight != null){
				newTrackPosition = (timeline2.draggedStuckToRight.trackStartPosition-timeline2.draggedTrackItem.mediaDuration)-1;
				timeline2.itemDraggedOffset = x-src.screens.editorScreen.timeline.track.TrackManager.trackPositionToPixelPosition(timeline2.draggedTrackItem.trackStartPosition);

			}

			timeline2.itemDraggedLastMouseX = x;
			timeline2.draggedTrackItem.trackStartPosition = newTrackPosition;
			
		
			
			src.screens.editorScreen.timeline.track.transition.TransitionManager.checkItem(timeline2.draggedTrackItem);
			src.screens.editorScreen.timeline.TimelineManager.updateTimelineLength();
			
			





			//System.out.println("new Position: "+newTrackPosition);

			//System.out.println("====================");


		}

		// timeline2.itemDraggedOffset = Timeline.pixelPositionToTrackPosition(x)-timeline2.selectedTrackItem.trackStartPosition;


		// System.out.println("offset: "+timeline2.itemDraggedOffset);

		//timeline2.selectedTrackItem.trackStartPosition += Timeline.pixelPositionToTrackPosition(x)-timeline2.selectedTrackItem.trackStartPosition;



		// System.out.println(timeline2.selectedTrackItem.trackStartPosition);

		/*if(timeline2.hoveredTrackItem == timeline2.selectedTrackItem){
    	int difference = x-timeline2.itemDraggedX;
    	timeline2.itemDraggedX = x;


    	timeline2.selectedTrackItem.trackStartPosition += difference;
    }*/









	}//GEN-LAST:event_timeline2MouseDragged

	private void timeline2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeline2MouseReleased


		int x = evt.getX();
		int y = evt.getY();

		if(!src.screens.editorScreen.timeline.track.TrackManager.validTrackItemPosition(timeline2.draggedTrackItem) && timeline2.croppedTrackItem == null){
			timeline2.draggedTrackItem.trackStartPosition = timeline2.itemDraggedLastX;
			src.screens.editorScreen.timeline.track.TrackManager.moveItemToNewTrack(timeline2.draggedTrackItem, timeline2.itemDraggedLastTrack);
		}
		
		if(!src.screens.editorScreen.timeline.track.TrackManager.validTrackItemPosition(timeline2.croppedTrackItem)){
			timeline2.croppedTrackItem.mediaDuration = timeline2.itemCroppedLastDuration;
		}


		if(timeline2.draggedTrackItem != null){
			if(timeline2.draggedTrackItem.trackStartPosition == timeline2.itemDraggedLastX && src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(timeline2.draggedTrackItem) == timeline2.itemDraggedLastTrack){
				src.screens.editorScreen.timeline.TimelineManager.timelinePosition = src.screens.editorScreen.timeline.track.TrackManager.pixelPositionToTrackPosition(x);
				ImagePanel.generateFullQualityFrame();
			} else {
				for(int frameIndex = timeline2.draggedTrackItem.trackStartPosition; frameIndex < timeline2.draggedTrackItem.trackStartPosition+timeline2.draggedTrackItem.mediaDuration+1; frameIndex++){
					if(frameIndex+1 < Renderer.renderingStatus.length){
					Renderer.renderingStatus[frameIndex] = 0;
					ImagePanel.generateFullQualityFrame();
					}
				}
				
				for(int frameIndex = timeline2.itemDraggedLastX; frameIndex < timeline2.itemDraggedLastX+timeline2.draggedTrackItem.mediaDuration+1; frameIndex++){
					if(frameIndex+1 < Renderer.renderingStatus.length){
						Renderer.renderingStatus[frameIndex] = 0;
						ImagePanel.generateFullQualityFrame();
					}
				}
			}

		} else {
			if(timeline2.croppedTrackItem == null){//stops position moving when cropping
			src.screens.editorScreen.timeline.TimelineManager.timelinePosition = src.screens.editorScreen.timeline.track.TrackManager.pixelPositionToTrackPosition(x);
			ImagePanel.generateFullQualityFrame();
			}
		}


		
	
		timeline2.croppedTrackItem = null;
		timeline2.itemCroppedLastDuration = 0;

		timeline2.draggedTrackItem = null;
		timeline2.draggedStuckToLeft = null;
		timeline2.draggedStickDelayLeft = 0;
		
		timeline2.draggedStuckToRight = null;
		timeline2.draggedStickDelayRight = 0;
		
		timeline2.itemDraggedOffset = 0;
		timeline2.itemDraggedLastX = 0;
		timeline2.itemDraggedLastTrack = null;
		
		
		if(TransitionMediaPanel.hoveredTransition != TransitionMediaPanel.draggedTransition && TransitionMediaPanel.draggedTransition != -1){
			
			
			boolean notNull = TransitionMediaPanel.item1 != null && TransitionMediaPanel.item2 != null;
			
			if(TransitionMediaPanel.transitionValidPosition && notNull){
				
				
				Track track1 = src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(TransitionMediaPanel.item1);
				Track track2 = src.screens.editorScreen.timeline.track.TrackManager.getTrackForItem(TransitionMediaPanel.item2);
				
				
				if(track1 == track2 && TransitionMediaPanel.item1.trackStartPosition < TransitionMediaPanel.item2.trackStartPosition){
				
				
				TrackObject trackObject = null;
				if(TransitionMediaPanel.draggedTransition == 0){
					trackObject = new Fade(TransitionMediaPanel.item1, TransitionMediaPanel.item2);
				}
				
				if(TransitionMediaPanel.draggedTransition == 1){
					trackObject = new WipeDown(TransitionMediaPanel.item1, TransitionMediaPanel.item2);
				}
				
				
				Transition transition = (Transition) trackObject;
				
				transition.duration = 50;
				
				

				
				track1.transitions.add(transition);
				
				
				for(int frameIndex = transition.item1.trackStartPosition; frameIndex < transition.item2.trackStartPosition+transition.item2.mediaDuration+1; frameIndex++){
					Renderer.renderingStatus[frameIndex] = 0;
				}
				
				System.out.println("added");
				}
				
				
			} else {
				
				
				System.out.println("Wrong place!! "+TransitionMediaPanel.draggedTransition);
			}
			TransitionMediaPanel.draggedTransition = -1;
		}
		
		
		
		if(MediaPanel.hoveredMediaItem != MediaPanel.draggedMediaItem && MediaPanel.draggedMediaItem != null && evt.getButton() != 3){
			
			

			
			if(MediaPanel.transitionValidPosition){
				

				int distance = src.screens.editorScreen.timeline.track.TrackManager.getDistanceFromNextItem(MediaPanel.track, MediaPanel.xPosition);
				
				
				int size = 100;
				
				if(MediaPanel.draggedMediaItem instanceof MediaVideoItem){
					MediaVideoItem mediaVideoItem = (MediaVideoItem)MediaPanel.draggedMediaItem;
					size = mediaVideoItem.totalFrames;
				}

				
				
				if(distance < size && distance != -1){
					MediaPanel.track.addTrackItem(MediaPanel.draggedMediaItem, 0, distance-1, MediaPanel.xPosition);
				} else {
					MediaPanel.track.addTrackItem(MediaPanel.draggedMediaItem, 0, size, MediaPanel.xPosition);
				}

				
				//if(distance != -1){
					
				//}
				
				System.out.println("added: "+distance);

				
				
			} else {
				
				
				System.out.println("Wrong place!!2");
			}
			MediaPanel.draggedMediaItem = null;
		} else {
			MediaPanel.draggedMediaItem = null;
		}
				
				
		
		ImagePanel.update();
		DecodingThread.updateDecodingPriority();
	}//GEN-LAST:event_timeline2MouseReleased

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
	PlayEvent.playing = false;
	jToggleButton1.setSelected(false);
}//GEN-LAST:event_jButton1ActionPerformed

private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
	if(!jToggleButton1.isSelected()){
		jToggleButton1.setSelected(true);
	}
	PlayEvent.playing = true;
}//GEN-LAST:event_jToggleButton1ActionPerformed

private void transitionMediaPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transitionMediaPanel1MouseMoved
transitionMediaPanel1.setMousePosition(evt.getX(), evt.getY());
}//GEN-LAST:event_transitionMediaPanel1MouseMoved

private void transitionMediaPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transitionMediaPanel1MousePressed
	transitionMediaPanel1.mousePressed(evt.getX(), evt.getY());
}//GEN-LAST:event_transitionMediaPanel1MousePressed

private void transitionMediaPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transitionMediaPanel1MouseReleased
	
	
	//System.out.print("button: "+evt.getButton());
	
	if(evt.getButton() == 3){
		TransitionMediaPanel.draggedTransition = -1;
		System.out.println("cancelled");
	}
	
	
}//GEN-LAST:event_transitionMediaPanel1MouseReleased

private void transitionMediaPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transitionMediaPanel1MouseDragged
	transitionMediaPanel1.setMousePosition(evt.getX(), evt.getY());
	
	if(TransitionMediaPanel.draggedTransition != -1){
		if(TransitionMediaPanel.hoveredTransition != TransitionMediaPanel.draggedTransition){
			
		}
	}
	
}//GEN-LAST:event_transitionMediaPanel1MouseDragged

private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged

}//GEN-LAST:event_formMouseDragged

private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_jTextField1KeyPressed

private void timeline2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_timeline2KeyPressed

	//System.out.println("fsdfdf");
	
	timeline2.keyPressed(evt.getKeyCode());
}//GEN-LAST:event_timeline2KeyPressed

private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
// TODO add your handling code here:
}//GEN-LAST:event_formMousePressed

private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
System.out.println("dfsdfsdf");
}//GEN-LAST:event_formKeyPressed

private void timeline2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_timeline2KeyReleased
// TODO add your handling code here:
}//GEN-LAST:event_timeline2KeyReleased

private void jMenuBar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jMenuBar1KeyPressed
System.out.println("hfghfghfgh");
}//GEN-LAST:event_jMenuBar1KeyPressed

private void jSplitPane2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jSplitPane2KeyPressed
System.out.println("asdfgdfdfg");
}//GEN-LAST:event_jSplitPane2KeyPressed

private void customRealButton1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton1MouseReleased

}//GEN-LAST:event_customRealButton1MouseReleased

private void jLayeredPane1AncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_jLayeredPane1AncestorResized
	TaskThread.updateSizes();
}//GEN-LAST:event_jLayeredPane1AncestorResized

private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
	ExportManager.selectedVideoSizeOption = 0;
	ExportManager.updateVideoSizePanel();
}//GEN-LAST:event_jRadioButton1ActionPerformed

private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
	ExportManager.selectedVideoSizeOption = 1;
	ExportManager.updateVideoSizePanel();
}//GEN-LAST:event_jRadioButton3ActionPerformed

private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
	ExportManager.selectedVideoSizeOption = 2;
	ExportManager.updateVideoSizePanel();
}//GEN-LAST:event_jRadioButton4ActionPerformed

private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jCheckBox3ActionPerformed

private void customRealButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton1ActionPerformed
	ExportManager.startExport();
}//GEN-LAST:event_customRealButton1ActionPerformed

private void customRealButton2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton2MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton2MouseReleased

private void customRealButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton2ActionPerformed
	MediaManager.importMedia();
}//GEN-LAST:event_customRealButton2ActionPerformed

private void customRealButton3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton3MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton3MouseReleased

private void customRealButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton3ActionPerformed
MediaManager.addAllToTimeline();
}//GEN-LAST:event_customRealButton3ActionPerformed

private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
MediaManager.selectedAlbum = jComboBox3.getSelectedIndex();
refreshAlbumNameList();
}//GEN-LAST:event_jComboBox3ActionPerformed

private void customRealButton4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton4MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton4MouseReleased

private void customRealButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton4ActionPerformed
	MediaManager.importFolder();
}//GEN-LAST:event_customRealButton4ActionPerformed

private void customRealButton5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton5MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton5MouseReleased

private void customRealButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton5ActionPerformed
	String name = JOptionPane.showInputDialog(null, "Enter an album name (Max 30 characters)");
	if(name == null){//Make sure the iser 
		JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.INFORMATION_MESSAGE);
		return;
	}
	if(name.equals("")){//
		JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.INFORMATION_MESSAGE);
		return;
	}
	if(name.length() > 30){
		JOptionPane.showMessageDialog(null, "Name too long!", "Error", JOptionPane.INFORMATION_MESSAGE);
		return;
	}
	
	MediaPanelManager.createAlbum(name, true);
	
	
}//GEN-LAST:event_customRealButton5ActionPerformed

private void mediaPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mediaPanel1MouseMoved
	mediaPanel1.setMousePosition(evt.getX(), evt.getY());
}//GEN-LAST:event_mediaPanel1MouseMoved

private void mediaPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mediaPanel1MousePressed
	mediaPanel1.mousePressed(evt.getX(), evt.getY());
}//GEN-LAST:event_mediaPanel1MousePressed

private void mediaPanel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mediaPanel1MouseReleased
	if(evt.getButton() == 3){
		mediaPanel1.draggedMediaItem = null;
	}
}//GEN-LAST:event_mediaPanel1MouseReleased

private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
	if(MediaManager.getSelectedAlbum() != null){
		MediaManager.getSelectedAlbum().albumName = jTextField3.getText();
		
		
		refreshAlbumNameList();
	}
}//GEN-LAST:event_jTextField3KeyReleased

private void customRealButton6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton6MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton6MouseReleased

private void customRealButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton6ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton6ActionPerformed

private void customRealButton7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton7MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton7MouseReleased

private void customRealButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton7ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton7ActionPerformed

private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jCheckBox4ActionPerformed

private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jCheckBox5ActionPerformed

private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
	NewProject.open(false);
}//GEN-LAST:event_jButton6ActionPerformed

private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
	MainApplet.getInstance().getjPanel15().setVisible(false);
	MainApplet.getInstance().getjPanel16().setVisible(false);
	MainApplet.getInstance().getjPanel30().setVisible(false);
	DataManager.loadFromFile();
}//GEN-LAST:event_jButton7ActionPerformed

private void customRealButton8MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton8MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton8MouseReleased

private void customRealButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton8ActionPerformed
	NewProject.ok();
}//GEN-LAST:event_customRealButton8ActionPerformed

private void customRealButton9MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton9MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton9MouseReleased

private void customRealButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton9ActionPerformed

	NewProject.cancel();
}//GEN-LAST:event_customRealButton9ActionPerformed

private void jTextField11KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11KeyReleased
NewProject.projectName = jTextField11.getText();
NewProject.refresh();
}//GEN-LAST:event_jTextField11KeyReleased

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
NewProject.open(true);
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
	DataManager.loadFromFile();
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
System.out.println("save project");
}//GEN-LAST:event_jMenuItem3ActionPerformed

private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
	DataManager.saveDataToFile();
}//GEN-LAST:event_jMenuItem4ActionPerformed

private void customButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customButton2ActionPerformed
	PreviewPanelManager.selectedItemView = true;
	PreviewPanelManager.refreshViewModeButtons();
}//GEN-LAST:event_customButton2ActionPerformed

private void customButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customButton1ActionPerformed
	PreviewPanelManager.selectedItemView = false;
	PreviewPanelManager.refreshViewModeButtons();
}//GEN-LAST:event_customButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
TimelineManager.setTimelinePosition(0);
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
TimelineManager.setTimelinePosition((Renderer.pixels.length-1));
}//GEN-LAST:event_jButton3ActionPerformed

private void customRealButton10MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton10MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton10MouseReleased

private void customRealButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton10ActionPerformed
	ChromaKey.newColourProfile();
}//GEN-LAST:event_customRealButton10ActionPerformed

private void customRealButton11MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton11MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton11MouseReleased

private void customRealButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton11ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton11ActionPerformed

private void customRealButton12MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton12MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton12MouseReleased

private void customRealButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton12ActionPerformed
ChromaKey.addColour();
}//GEN-LAST:event_customRealButton12ActionPerformed

private void customRealButton13MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton13MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton13MouseReleased

private void customRealButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton13ActionPerformed
	ChromaKey.newTrigger();
}//GEN-LAST:event_customRealButton13ActionPerformed

private void customRealButton14MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton14MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton14MouseReleased

private void customRealButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton14ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton14ActionPerformed

private void customRealButton15MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton15MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton15MouseReleased

private void customRealButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton15ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton15ActionPerformed

private void customRealButton16MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton16MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton16MouseReleased

private void customRealButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton16ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton16ActionPerformed

private void customRealButton17MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton17MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton17MouseReleased

private void customRealButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton17ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton17ActionPerformed

private void customRealButton18MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton18MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton18MouseReleased

private void customRealButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton18ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton18ActionPerformed

private void customRealButton19MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton19MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton19MouseReleased

private void customRealButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton19ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton19ActionPerformed

private void customRealButton20MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton20MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton20MouseReleased

private void customRealButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton20ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton20ActionPerformed

private void customRealButton21MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton21MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton21MouseReleased

private void customRealButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton21ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton21ActionPerformed

private void customRealButton22MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customRealButton22MouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton22MouseReleased

private void customRealButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customRealButton22ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_customRealButton22ActionPerformed

private void jComboBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox11ActionPerformed

	
	ChromaKey.selectedColourProfile = jComboBox11.getSelectedIndex();
	ChromaKey.refreshProfileNameList();
	
	
}//GEN-LAST:event_jComboBox11ActionPerformed

private void jComboBox13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox13ActionPerformed
	ChromaKey.selectedTrigger = jComboBox13.getSelectedIndex();
	ChromaKey.refreshTriggerNameList();
}//GEN-LAST:event_jComboBox13ActionPerformed

private void jComboBox12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox12ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jComboBox12ActionPerformed

private void jComboBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox14ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jComboBox14ActionPerformed

private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased
	if(ColourProfileManager.getSelectedColourProfileInstance() != null){
		ColourProfileManager.getSelectedColourProfileInstance().name = jTextField8.getText();
		
		
		ChromaKey.refreshProfileNameList();
	}
}//GEN-LAST:event_jTextField8KeyReleased

private void jTextField9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyReleased
	if(TriggerManager.getSelectedTrigger() != null){
		TriggerManager.getSelectedTrigger().name = jTextField9.getText();
		
		
		ChromaKey.refreshTriggerNameList();
	}
}//GEN-LAST:event_jTextField9KeyReleased

private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jCheckBox6ActionPerformed

private void chromaKeyFrame1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chromaKeyFrame1MouseMoved
	int x = evt.getX();
	int y = evt.getY();
	
	chromaKeyFrame1.setMousePosition(x,y);
	
}//GEN-LAST:event_chromaKeyFrame1MouseMoved

private void chromaKeyFrame1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_chromaKeyFrame1MouseReleased
	int x = evt.getX();
	int y = evt.getY();
	chromaKeyFrame1.mousePressed(x, y);
}//GEN-LAST:event_chromaKeyFrame1MouseReleased


public void refreshAlbumNameList(){

	if(MediaManager.getSelectedAlbum() != null){
		jTextField3.setText(MediaManager.getSelectedAlbum().albumName);
		jTextField3.setEnabled(true);
	} else {
		jTextField3.setText("");
		jTextField3.setEnabled(false);
	}

	String[] names = new String[MediaPanelManager.albums.size()];

	for(int index = 0; index < MediaPanelManager.albums.size(); index++){
		if(MediaPanelManager.albums.get(index).albumName.length() == 0){
			names[index] = "Unnamed";
		} else {
			names[index] = MediaPanelManager.albums.get(index).albumName;
		}
	}
	jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(names));
	jComboBox3.setSelectedIndex(MediaManager.selectedAlbum);

}

	private void jScrollPane2Change(ChangeEvent e) {
		timeline2.repaint();
	}

	public void setTimeline1(src.Timeline timeline1) {
		//this.timeline1 = timeline1;
	}

	public src.Timeline getTimeline1() {
		return null;
	}



	public void setjScrollPane2(javax.swing.JScrollPane jScrollPane2) {
		this.jScrollPane2 = jScrollPane2;
	}

	public javax.swing.JScrollPane getjScrollPane2() {
		return jScrollPane2;
	}



	public void setTrackManagerPanel1(src.TrackManagerPanel trackManagerPanel1) {
		this.trackManagerPanel1 = trackManagerPanel1;
	}

	public src.TrackManagerPanel getTrackManagerPanel1() {
		return trackManagerPanel1;
	}



	public void setTimeline2(src.Timeline timeline2) {
		this.timeline2 = timeline2;
	}

	public src.Timeline getTimeline2() {
		return timeline2;
	}



	public void setImagePanel1(src.ImagePanel imagePanel1) {
		this.imagePanel1 = imagePanel1;
	}

	public src.ImagePanel getImagePanel1() {
		return imagePanel1;
	}
	
	/**
	 * Registers an event.
	 * @param event
	 */
	public void registerEvent(Event event) {
		eventsToAdd.add(event);
	}
	public List<Event> events;
	public List<Event> eventsToAdd;
	public List<Event> eventsToRemove;
	
	/**
	 * Processes any pending events.
	 */
	public void processEvents() {
		for(Event e : eventsToAdd) {
			events.add(e);
		}
		eventsToAdd.clear();
		for(Event e : events) {
			if(e.isStopped()) {
				eventsToRemove.add(e);
			} else if(e.isReady()) {
				e.run();
			}
		}
		for(Event e : eventsToRemove) {
			events.remove(e);
		}
		eventsToRemove.clear();
	}



    public void setjTextField1(javax.swing.JTextField jTextField1) {
		this.jTextField1 = jTextField1;
	}

	public javax.swing.JTextField getjTextField1() {
		return jTextField1;
	}
    public void setTransitionMediaPanel1(src.TransitionMediaPanel transitionMediaPanel1) {
		this.transitionMediaPanel1 = transitionMediaPanel1;
	}

	public src.TransitionMediaPanel getTransitionMediaPanel1() {
		return transitionMediaPanel1;
	}
    public void setjList1(javax.swing.JList jList1) {
		this.jList1 = jList1;
	}

	public javax.swing.JList getjList1() {
		return jList1;
	}
	public void setjLabel3(javax.swing.JLabel jLabel3) {
		this.jLabel3 = jLabel3;
	}

	public javax.swing.JLabel getjLabel3() {
		return jLabel3;
	}
	public void setjLabel2(javax.swing.JLabel jLabel2) {
		this.jLabel2 = jLabel2;
	}

	public javax.swing.JLabel getjLabel2() {
		return jLabel2;
	}
	public void setjPanel14(javax.swing.JPanel jPanel14) {
		this.jPanel14 = jPanel14;
	}

	public javax.swing.JPanel getjPanel14() {
		return jPanel14;
	}

    public void setjLayeredPane1(javax.swing.JLayeredPane jLayeredPane1) {
		this.jLayeredPane1 = jLayeredPane1;
	}

	public javax.swing.JLayeredPane getjLayeredPane1() {
		return jLayeredPane1;
	}
   
    public void setjSplitPane1(javax.swing.JSplitPane jSplitPane1) {
		this.jSplitPane1 = jSplitPane1;
	}

	public javax.swing.JSplitPane getjSplitPane1() {
		return jSplitPane1;
	}
    public void setIntroScreen2(src.IntroScreen introScreen2) {
		this.introScreen2 = introScreen2;
	}

	public src.IntroScreen getIntroScreen2() {
		return introScreen2;
	}
	public void setjPanel15(javax.swing.JPanel jPanel15) {
		this.jPanel15 = jPanel15;
	}

	public javax.swing.JPanel getjPanel15() {
		return jPanel15;
	}
    public void setjPanel16(javax.swing.JPanel jPanel16) {
		this.jPanel16 = jPanel16;
	}

	public javax.swing.JPanel getjPanel16() {
		return jPanel16;
	}

    public void setjScrollPane3(javax.swing.JScrollPane jScrollPane3) {
		this.jScrollPane3 = jScrollPane3;
	}

	public javax.swing.JScrollPane getjScrollPane3() {
		return jScrollPane3;
	}
    public void setjRadioButton1(javax.swing.JRadioButton jRadioButton1) {
		this.jRadioButton1 = jRadioButton1;
	}

	public javax.swing.JRadioButton getjRadioButton1() {
		return jRadioButton1;
	}
	public void setjRadioButton3(javax.swing.JRadioButton jRadioButton3) {
		this.jRadioButton3 = jRadioButton3;
	}

	public javax.swing.JRadioButton getjRadioButton3() {
		return jRadioButton3;
	}
	public void setjRadioButton4(javax.swing.JRadioButton jRadioButton4) {
		this.jRadioButton4 = jRadioButton4;
	}

	public javax.swing.JRadioButton getjRadioButton4() {
		return jRadioButton4;
	}
	public void setjComboBox2(javax.swing.JComboBox jComboBox2) {
		this.jComboBox2 = jComboBox2;
	}

	public javax.swing.JComboBox getjComboBox2() {
		return jComboBox2;
	}
	public void setjTextField4(javax.swing.JTextField jTextField4) {
		this.jTextField4 = jTextField4;
	}

	public javax.swing.JTextField getjTextField4() {
		return jTextField4;
	}
	public void setjCheckBox3(javax.swing.JCheckBox jCheckBox3) {
		this.jCheckBox3 = jCheckBox3;
	}

	public javax.swing.JCheckBox getjCheckBox3() {
		return jCheckBox3;
	}
	public void setjLabel7(javax.swing.JLabel jLabel7) {
		this.jLabel7 = jLabel7;
	}

	public javax.swing.JLabel getjLabel7() {
		return jLabel7;
	}
	public void setjLabel6(javax.swing.JLabel jLabel6) {
		this.jLabel6 = jLabel6;
	}

	public javax.swing.JLabel getjLabel6() {
		return jLabel6;
	}
	public void setjTextField5(javax.swing.JTextField jTextField5) {
		this.jTextField5 = jTextField5;
	}

	public javax.swing.JTextField getjTextField5() {
		return jTextField5;
	}
    public void setjSlider3(javax.swing.JSlider jSlider3) {
		this.jSlider3 = jSlider3;
	}

	public javax.swing.JSlider getjSlider3() {
		return jSlider3;
	}
    public void setMediaPanel1(src.MediaPanel mediaPanel1) {
		this.mediaPanel1 = mediaPanel1;
	}

	public src.MediaPanel getMediaPanel1() {
		return mediaPanel1;
	}
    public void setjPanel24(javax.swing.JPanel jPanel24) {
		this.jPanel24 = jPanel24;
	}

	public javax.swing.JPanel getjPanel24() {
		return jPanel24;
	}
	public void setjScrollPane8(javax.swing.JScrollPane jScrollPane8) {
		this.jScrollPane8 = jScrollPane8;
	}

	public javax.swing.JScrollPane getjScrollPane8() {
		return jScrollPane8;
	}
    public void setjLabel10(javax.swing.JLabel jLabel10) {
		this.jLabel10 = jLabel10;
	}

	public javax.swing.JLabel getjLabel10() {
		return jLabel10;
	}
	public static void setInstance(MainApplet instance) {
		MainApplet.instance = instance;
	}

	public static MainApplet getInstance() {
		return instance;
	}
	public void setjInternalFrame1(javax.swing.JInternalFrame jInternalFrame1) {
		this.jInternalFrame1 = jInternalFrame1;
	}

	public javax.swing.JInternalFrame getjInternalFrame1() {
		return jInternalFrame1;
	}
    public void setjTextField6(javax.swing.JTextField jTextField6) {
		this.jTextField6 = jTextField6;
	}

	public javax.swing.JTextField getjTextField6() {
		return jTextField6;
	}
    public void setjLabel19(javax.swing.JLabel jLabel19) {
		this.jLabel19 = jLabel19;
	}

	public javax.swing.JLabel getjLabel19() {
		return jLabel19;
	}
	public void setjLabel20(javax.swing.JLabel jLabel20) {
		this.jLabel20 = jLabel20;
	}

	public javax.swing.JLabel getjLabel20() {
		return jLabel20;
	}
	public void setjProgressBar1(javax.swing.JProgressBar jProgressBar1) {
		this.jProgressBar1 = jProgressBar1;
	}

	public javax.swing.JProgressBar getjProgressBar1() {
		return jProgressBar1;
	}
    public void setjInternalFrame2(javax.swing.JInternalFrame jInternalFrame2) {
		this.jInternalFrame2 = jInternalFrame2;
	}

	public javax.swing.JInternalFrame getjInternalFrame2() {
		return jInternalFrame2;
	}
    public void setjPanel30(javax.swing.JPanel jPanel30) {
		this.jPanel30 = jPanel30;
	}

	public javax.swing.JPanel getjPanel30() {
		return jPanel30;
	}
	public void setjComboBox9(javax.swing.JComboBox jComboBox9) {
		this.jComboBox9 = jComboBox9;
	}

	public javax.swing.JComboBox getjComboBox9() {
		return jComboBox9;
	}

	public void setjPanel32(javax.swing.JPanel jPanel32) {
		this.jPanel32 = jPanel32;
	}

	public javax.swing.JPanel getjPanel32() {
		return jPanel32;
	}

    public void setjLabel25(javax.swing.JLabel jLabel25) {
		this.jLabel25 = jLabel25;
	}

	public javax.swing.JLabel getjLabel25() {
		return jLabel25;
	}
    public void setjTextField11(javax.swing.JTextField jTextField11) {
		this.jTextField11 = jTextField11;
	}

	public javax.swing.JTextField getjTextField11() {
		return jTextField11;
	}
    public void setjComboBox10(javax.swing.JComboBox jComboBox10) {
		this.jComboBox10 = jComboBox10;
	}

	public javax.swing.JComboBox getjComboBox10() {
		return jComboBox10;
	}
    public void setjTextField2(javax.swing.JTextField jTextField2) {
		this.jTextField2 = jTextField2;
	}

	public javax.swing.JTextField getjTextField2() {
		return jTextField2;
	}
    public void setjLabel21(javax.swing.JLabel jLabel21) {
		this.jLabel21 = jLabel21;
	}

	public javax.swing.JLabel getjLabel21() {
		return jLabel21;
	}
    public void setjProgressBar2(javax.swing.JProgressBar jProgressBar2) {
		this.jProgressBar2 = jProgressBar2;
	}

	public javax.swing.JProgressBar getjProgressBar2() {
		return jProgressBar2;
	}
	public void setjLabel23(javax.swing.JLabel jLabel23) {
		this.jLabel23 = jLabel23;
	}

	public javax.swing.JLabel getjLabel23() {
		return jLabel23;
	}
    public void setCustomButton2(src.CustomButton customButton2) {
		this.customButton2 = customButton2;
	}

	public src.CustomButton getCustomButton2() {
		return customButton2;
	}
	public void setCustomButton1(src.CustomButton customButton1) {
		this.customButton1 = customButton1;
	}

	public src.CustomButton getCustomButton1() {
		return customButton1;
	}
    public void setjTextField8(javax.swing.JTextField jTextField8) {
		this.jTextField8 = jTextField8;
	}

	public javax.swing.JTextField getjTextField8() {
		return jTextField8;
	}
	public void setjComboBox11(javax.swing.JComboBox jComboBox11) {
		this.jComboBox11 = jComboBox11;
	}

	public javax.swing.JComboBox getjComboBox11() {
		return jComboBox11;
	}
	public void setjComboBox13(javax.swing.JComboBox jComboBox13) {
		this.jComboBox13 = jComboBox13;
	}

	public javax.swing.JComboBox getjComboBox13() {
		return jComboBox13;
	}
	public void setjTextField9(javax.swing.JTextField jTextField9) {
		this.jTextField9 = jTextField9;
	}

	public javax.swing.JTextField getjTextField9() {
		return jTextField9;
	}
    public void setChromaKeyFrame1(src.ChromaKeyFrame chromaKeyFrame1) {
		this.chromaKeyFrame1 = chromaKeyFrame1;
	}

	public src.ChromaKeyFrame getChromaKeyFrame1() {
		return chromaKeyFrame1;
	}
    public void setjComboBox12(javax.swing.JComboBox jComboBox12) {
		this.jComboBox12 = jComboBox12;
	}

	public javax.swing.JComboBox getjComboBox12() {
		return jComboBox12;
	}
	// Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JInternalFrame chromaKey;
    private src.ChromaKeyFrame chromaKeyFrame1;
    private src.CustomButton customButton1;
    private src.CustomButton customButton2;
    private src.CustomRealButton customRealButton1;
    private src.CustomRealButton customRealButton10;
    private src.CustomRealButton customRealButton11;
    private src.CustomRealButton customRealButton12;
    private src.CustomRealButton customRealButton13;
    private src.CustomRealButton customRealButton14;
    private src.CustomRealButton customRealButton15;
    private src.CustomRealButton customRealButton16;
    private src.CustomRealButton customRealButton17;
    private src.CustomRealButton customRealButton18;
    private src.CustomRealButton customRealButton19;
    private src.CustomRealButton customRealButton2;
    private src.CustomRealButton customRealButton20;
    private src.CustomRealButton customRealButton21;
    private src.CustomRealButton customRealButton22;
    private src.CustomRealButton customRealButton3;
    private src.CustomRealButton customRealButton4;
    private src.CustomRealButton customRealButton5;
    private src.CustomRealButton customRealButton6;
    private src.CustomRealButton customRealButton7;
    private src.CustomRealButton customRealButton8;
    private src.CustomRealButton customRealButton9;
    private javax.swing.JPanel dataDownloader_mainPanel;
    private src.ImagePanel imagePanel1;
    private src.IntroScreen introScreen2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox12;
    private javax.swing.JComboBox jComboBox13;
    private javax.swing.JComboBox jComboBox14;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSlider jSlider3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JToggleButton jToggleButton1;
    private src.MediaPanel mediaPanel1;
    private src.Timeline timeline2;
    private src.TrackManagerPanel trackManagerPanel1;
    private src.TransitionMediaPanel transitionMediaPanel1;
    private src.VideoSlider videoSlider1;
    // End of variables declaration//GEN-END:variables
}