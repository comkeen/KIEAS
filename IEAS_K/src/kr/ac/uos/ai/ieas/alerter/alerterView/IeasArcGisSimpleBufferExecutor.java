package kr.ac.uos.ai.ieas.alerter.alerterView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import com.esri.client.local.GPServiceType;
import com.esri.client.local.LocalGeoprocessingService;
import com.esri.client.local.LocalServiceStartCompleteEvent;
import com.esri.client.local.LocalServiceStartCompleteListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Geometry.Type;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.Style;
import com.esri.core.tasks.ags.geoprocessing.GPFeatureRecordSetLayer;
import com.esri.core.tasks.ags.geoprocessing.GPLinearUnit;
import com.esri.core.tasks.ags.geoprocessing.GPParameter;
import com.esri.core.tasks.ags.geoprocessing.Geoprocessor;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.MapOverlay;
import com.esri.runtime.ArcGISRuntime;

public class IeasArcGisSimpleBufferExecutor extends MapOverlay{

	private JComponent contentPane;
	private JMap map;
	private SpatialReference srMap;
	private AtomicInteger tasksInProgress;
	private LocalGeoprocessingService simpleBufferService;
	private JTextField distanceInput;
	private IeasArcGisSimpleBufferExecutor simpleBufferExecutor;
	private static final String DEFAULT_BUFFER_DISTANCE = "500";
	private static final int PANEL_WIDTH = 230;
	private static final String FSP = System.getProperty("file.separator");
	public static final String SERVICE_NAME = "SimpleBuffer";

	private static final long serialVersionUID = 1L;
	JMap jMap;
	GraphicsLayer graphicsLayer;

	// symbology
	private final SimpleLineSymbol SYM_BORDER = new SimpleLineSymbol(Color.RED, 1);
	private final SimpleFillSymbol SYM_FILL = new SimpleFillSymbol(new Color(255, 0, 0, 80), SYM_BORDER);
	public final SimpleMarkerSymbol SYM_POINT = new SimpleMarkerSymbol(Color.MAGENTA, 14, Style.CIRCLE);
	private Point point;
	private Graphic pointGraphic;

	public IeasArcGisSimpleBufferExecutor(JMap jMap, GraphicsLayer graphicsLayer) {
		this.map = jMap;
		this.graphicsLayer = graphicsLayer;
		
		this.tasksInProgress = new AtomicInteger(0);
	}

	 @Override
	    public void onMouseClicked(MouseEvent mouseEvent) {

//	      if (!simpleBufferExecutor.isActive()) {
//	        return;
//	      }
		 SpatialReference mapSR = map.getSpatialReference();		
			System.out.println("The map spatial reference is wkid=" + mapSR.getID());
			point = GeometryEngine.project(126.984063, 37.535887, mapSR);
			PictureMarkerSymbol symbol = new PictureMarkerSymbol("http://static.arcgis.com/images/Symbols/Basic/RedShinyPin.png");
			pointGraphic = new Graphic(point, symbol);
			graphicsLayer.addGraphic(pointGraphic);
		 
		 
	      if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
	        graphicsLayer.removeAll();
	        return;
	      }

	      tasksInProgress.incrementAndGet();

	      // obtain the point from the mouse click
	      Point point = map.toMapPoint(mouseEvent.getX(), mouseEvent.getY());
	      // obtain the buffer distance from the text field
	      double distance = Double.parseDouble(distanceInput.getText());
	      if (distance > 2000 || distance < 50) {
	        JOptionPane.showMessageDialog(contentPane,
	            "Please enter a value from 50 to 2000.", "", JOptionPane.WARNING_MESSAGE);
	        return;
	      }

	      // use the point from the mouse click and add a graphic
	      Graphic pointGraphic = new Graphic(point, SYM_POINT);
	      graphicsLayer.addGraphic(pointGraphic);

	      // execute the buffer and display the buffer zone
	      executeSimpleBuffer(pointGraphic, distance);
	    }

	
	public AtomicInteger getTaskInprogress(){
		return tasksInProgress;
	}

	public void executeSimpleBuffer(Graphic pointGraphic, double bufferDistance) {

		System.out.println("executeSimpleBuffer");
		// create a Geoprocessor that points to the geoprocessing service URL
		Geoprocessor geoprocessor = new Geoprocessor(simpleBufferService.getUrlGeoprocessingService() + "/" + SERVICE_NAME);
		geoprocessor.setProcessSR(srMap);
		geoprocessor.setOutSR(srMap);

		// initialize the required input parameters: refer to help link in the
		// geoprocessing service URL for a list of required parameters
		List<GPParameter> parameters = new ArrayList<>();

		GPFeatureRecordSetLayer pointParam = new GPFeatureRecordSetLayer("InputFeatures");
		pointParam.setGeometryType(Type.POINT);
		pointParam.setSpatialReference(srMap);
		pointParam.addGraphic(pointGraphic);

		GPLinearUnit distanceParam = new GPLinearUnit("Distance");
		distanceParam.setUnits("esriKilometers");
		distanceParam.setDistance(bufferDistance);

		parameters.add(pointParam);
		parameters.add(distanceParam);

		geoprocessor.executeAsync(parameters, new CallbackListener<GPParameter[]>() {

			@Override
			public void onError(Throwable th) {
				th.printStackTrace();
			}

			@Override
			public void onCallback(final GPParameter[] result) {
//						updateProgressBarUI(null, tasksInProgress.decrementAndGet() > 0);
				processResult(result);
			}
		});
	}

	public JComponent createUI(JComponent contentPane) {

	    // application content
	    this.contentPane = contentPane;

	    // user panel
	    JPanel panel = createUserPanel();

	    contentPane.add(panel);

	    return contentPane;
	  }
	
	/**
	 * Process result from geoprocessing execution.
	 * @param result output of geoprocessing execution.
	 */
	private void processResult(GPParameter[] result) {
		for (GPParameter outputParameter : result) {
			if (outputParameter instanceof GPFeatureRecordSetLayer) {
				GPFeatureRecordSetLayer gpLayer = (GPFeatureRecordSetLayer) outputParameter;
				Graphic resultGraphic = gpLayer.getGraphics().get(0);
				// get the graphic and add to the graphics layer
				Graphic theGraphic = new Graphic(resultGraphic.getGeometry(),SYM_FILL);
				// add to the graphics layer
				graphicsLayer.addGraphic(theGraphic);
			}
		}
	}


	private JTextArea createDescription() {
		JTextArea description = new JTextArea(
				"Left click on the map to create a point graphic and automatically " +
				"compute a buffer around the clicked point. Enter the desired buffer " +
				"distance in the box below. Right click to remove graphics.");
		description.setFont(new Font("Verdana", Font.PLAIN, 11));
		description.setForeground(Color.WHITE);
		description.setBackground(new Color(0, 0, 0, 180));
		description.setEditable(false);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		return description;
	}

	private JPanel createUserPanel() {

		// create description
		JTextArea description = createDescription();

		// label to input buffer distance
		JLabel lblDistance = new JLabel("Distance (km): ");
		lblDistance.setForeground(Color.BLACK);
//		lblDistance.setFont(new Font("Verdana", Font.BOLD, 12));
		lblDistance.setMaximumSize(new Dimension(120, 20));

		// text field to input number of days
		distanceInput = new JTextField(DEFAULT_BUFFER_DISTANCE, 5);
		distanceInput.setMaximumSize(new Dimension(30, 20));

		// group the above UI items into a panel
		final JPanel controlPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(controlPanel, BoxLayout.X_AXIS);
		controlPanel.setLayout(boxLayout);
		controlPanel.setSize(PANEL_WIDTH, 20);
		controlPanel.setBorder(BorderFactory.createEmptyBorder(2,5,2,5));
		controlPanel.setBackground(new Color(255, 255, 255, 255));
		controlPanel.add(lblDistance);
		controlPanel.add(distanceInput);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		panel.setLocation(10, 10);
		panel.setSize(PANEL_WIDTH, 140);
		panel.setBackground(new Color(0, 0, 0, 0));
		panel.setBorder(new LineBorder(Color.BLACK, 3, false));

		// group control and description in a panel
		panel.add(description, BorderLayout.CENTER);
		panel.add(controlPanel, BorderLayout.SOUTH);

		return panel;
	}
	public String getPathSampleData() {
		String dataPath = null;
		String javaPath = ArcGISRuntime.getInstallDirectory();
		if (javaPath != null) {
			if (!(javaPath.endsWith("/") || javaPath.endsWith("\\"))){
				javaPath += FSP;
			}
			dataPath = javaPath + "sdk" + FSP + "samples" + FSP + "data" + FSP;
		}
		File dataFile = new File(dataPath);
		if (!dataFile.exists()) { 
			dataPath = ".." + FSP + "data" + FSP;
		}
		return dataPath;
	}
}