
/* Copyright 2014 Esri
All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package kr.ac.uos.ai.ieas.alerterView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.esri.client.local.LocalGeoprocessingService;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.map.ArcGISTiledMapServiceLayer;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.LayerList;
import com.esri.map.MapOverlay;
import com.esri.runtime.ArcGISRuntime;
import com.esri.toolkit.bookmarks.JExtentBookmark;

/**
 * This application demonstrates how to use the toolkit's {@link JExtentBookmark}
 * to bookmark extents. The extent bookmarks are displayed in a draggable panel
 * on top of the map.
 */
public class IeasArcGisMap {

	private static String FSP = System.getProperty("file.separator");

	// JMap
	private JMap map;
	private JComponent panelComponent;	
	private MapActionListener mapActionListener;

//	private IeasArcGisSimpleBufferExecutor simpleBufferExecutor;
	private JLayeredPane contentPane;
	private GraphicsLayer graphicsLayer;
	// resources

	private JExtentBookmark extentBookmarks;
	private LocalGeoprocessingService simpleBufferService;
	private IeasUserCommandOverlay ieasUserCommandOverlay;

	private JPanel buttonPanel;
	private JButton symbolButton;

	private JButton userCommandButton;

	private SymbolOverlay symbolOverlay;

	  private static final int BUTTON_WIDTH = 160;
	  private static final int BUTTON_HEIGHT = 25;
	  private static final Color BG_COLOR = new Color(0, 0, 0, 150);
	// ------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------
	public IeasArcGisMap() {

		System.out.println("create map");
		// create the UI, including the map, for the application.
		try {
			this.panelComponent = createUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public JComponent getMapPane(){
		return this.panelComponent;
	}

	// ------------------------------------------------------------------------
	// Core functionality
	// ------------------------------------------------------------------------
	/**
	 * Creates and displays the UI, including the map, for this application.
	 */
	public JComponent createUI() throws Exception {
		// application content
		this.contentPane = createContentPane();

		// map
		this.createMap();

		createButtonPanel();
		// add simple buffer
		initSimpleBufferExecutor();
		// add extent bookmarks
	    initBookmark();
		// add user command overlay
	    initUserCommandOverlay();
	    
		contentPane.add(map);

		return contentPane;
	}
	
	private void createButtonPanel() {
	    this.buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    buttonPanel.setSize(BUTTON_WIDTH + 10, (BUTTON_HEIGHT*3) + (5*4));

	    // buttons
	    this.symbolButton = new JButton("Symbol Overlay");
	    symbolButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
	    symbolButton.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
	    symbolButton.addActionListener(mapActionListener);
	    
	    this.userCommandButton = new JButton("User Command Overlay");
	    userCommandButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
	    userCommandButton.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
	    userCommandButton.addActionListener(mapActionListener);
	    
	    buttonPanel.setBackground(BG_COLOR);
	    buttonPanel.add(symbolButton);
	    buttonPanel.add(Box.createRigidArea(new Dimension(0,5)));
	    buttonPanel.add(userCommandButton);
	    buttonPanel.add(Box.createRigidArea(new Dimension(0,5)));
	    buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		buttonPanel.setLocation(10, 20);
	    contentPane.add(buttonPanel);
	}
	private void initSymbolOverlay(){
		this.symbolOverlay = new SymbolOverlay(graphicsLayer);
		map.addMapOverlay(symbolOverlay);
	}
	
	private void initUserCommandOverlay() {
		this.ieasUserCommandOverlay = new IeasUserCommandOverlay(map, graphicsLayer);	
		map.addMapOverlay(ieasUserCommandOverlay);
	}

	private void initBookmark() {
		extentBookmarks = new JExtentBookmark(map, getBookmarksFilePath());
		extentBookmarks.setLocation(10, 10);
		contentPane.add(extentBookmarks);

		// add some bookmarks if there are none
		if (extentBookmarks.getBookmarkCount() < 1) {
			extentBookmarks.addBookmark(
					"Boston", new Envelope(-7915906.2, 5211780.3, -7907660.4, 5217280.3));
			extentBookmarks.addBookmark(
					"Tokyo", new Envelope(15529071.2, 4234491.2, 15595037.4, 4278491.103));
			extentBookmarks.addBookmark(
					"Cape Town", new Envelope(2043409.3, -4025517.2, 2059901, -4014517.168));
			extentBookmarks.addBookmark(
					"London", new Envelope(-19830.3, 6705685.4, -3338.6, 6716685.423));
		}
	}

	private void initSimpleBufferExecutor() {
//		this.simpleBufferExecutor = new IeasArcGisSimpleBufferExecutor(map, graphicsLayer);
//		simpleBufferExecutor.createUI(contentPane);
//		simpleBufferExecutor.setActive(false);
//	    
//	    map.addMapOverlay(simpleBufferExecutor);
	}

	// ------------------------------------------------------------------------
	// Static methods
	// ------------------------------------------------------------------------
	/**
	 * Starting point of this application.
	 * @param args arguments to this application.
	 */

	// ------------------------------------------------------------------------
	// Private methods
	// ------------------------------------------------------------------------
	/**
	 * Creates a map.
	 * @return a map.
	 */
	private void createMap() throws Exception {
		System.out.println(System.getProperty("java.library.path"));
		this.map = new JMap();
		map.setWrapAroundEnabled(true);
	    LayerList layers = map.getLayers();

		// add a base layer

		ArcGISTiledMapServiceLayer tiledLayer = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
		layers.add(tiledLayer);
		// graphics layer to add point and buffer graphics
		graphicsLayer = new GraphicsLayer();
		mapActionListener = new MapActionListener(this);
		graphicsLayer.setName("Marker graphics");
		layers.add(graphicsLayer);
		map.addMapEventListener(mapActionListener);
		
		// set an initial map extent
		map.setExtent(new Envelope(new Point(14200000, 4500000), 500000, 500000));
	}

	/**
	 * Creates a content pane.
	 * @return a content pane.
	 */
	private static JLayeredPane createContentPane() {
		JLayeredPane contentPane = new JLayeredPane();
		contentPane.setBounds(100, 100, 1000, 700);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setVisible(true);
		return contentPane;
	}

	/**
	 * Returns the absolute path of the bookmarks file.
	 * If the user does not have permission to create a new file in current execution
	 * directory, then the java.io.tmpdir will be used.
	 * @return the absolute path to the bookmarks file.
	 */
	private static String getBookmarksFilePath() {
		String relativeFilePath = "extent.bookmarks";
		String absoluteFilePath = null;
		// check if can create a file in the current execution directory.
		// can't use File.canWrite() because create is considered different from write. 
		try {
			File relativeFile = new File(relativeFilePath);
			try {
				relativeFile.createNewFile();
			} catch (Exception ex) {
				System.out.println("Failed to create bookmarks file at " + relativeFile.getAbsolutePath());
			}
			if (relativeFile.exists()) {
				absoluteFilePath = relativeFile.getAbsolutePath();
			} else {
				String tempDataDir = System.getProperty("java.io.tmpdir") + FSP + 
						"ArcGIS SDKs" + FSP + "java" + ArcGISRuntime.getAPIVersion() + 
						FSP + "sdk" + FSP + "samples" + FSP + "bookmarks";
				File tempFolder = new File(tempDataDir);
				if (!tempFolder.exists()) {
					tempFolder.mkdirs();
				}
				absoluteFilePath = tempDataDir + FSP + relativeFilePath;
				System.out.println("Bookmarks file will be created at " + absoluteFilePath);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return absoluteFilePath;
	}

	public GraphicsLayer getGraphicsLayer() {
		return graphicsLayer;
	}

	public MapOverlay getSymbolOverlay() {
		return symbolOverlay;		
	}
}
