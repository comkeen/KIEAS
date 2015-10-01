package kr.ac.uos.ai.ieas.alerterView;

import java.awt.Color;
import java.awt.event.MouseEvent;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Segment;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.Style;
import com.esri.map.GraphicsLayer;
import com.esri.map.JMap;
import com.esri.map.MapOverlay;

public class IeasUserCommandOverlay extends MapOverlay {

    private static final long serialVersionUID = 1L;
    
    // buffer distance: will be in the map's units, meters in this case
    private static final double BUFFER_DISTANCE = 10000; // 10 km
    // symbology
    final static SimpleLineSymbol SYM_LINE   = new SimpleLineSymbol(Color.RED, 2.0f);
    final static SimpleMarkerSymbol SYM_POINT = new SimpleMarkerSymbol(new Color(200, 0, 0, 200), 8, Style.CIRCLE);
    final static SimpleFillSymbol SYM_BUFFER = new SimpleFillSymbol(new Color(0, 0, 255, 80), SYM_LINE);
    // map
    JMap jMap;
    // layer to which graphics will be added to
    GraphicsLayer gLayer;

    Polyline polyLine = new Polyline();
    Point    prevPoint;
    boolean  startOver = false;
    Geometry bufferedArea = null;

    /**
     * Constructor
     * @param jMap JMap to which this overlay belongs.
     * @param graphicsLayer
     */
    public IeasUserCommandOverlay(JMap jMap, GraphicsLayer graphicsLayer) {
      this.jMap = jMap;
      this.gLayer = graphicsLayer;
    }

    /**
     * Handle mouse-clicks.
     * On left-click - draws either a polyline or a point.
     * On right-click - computes and draws the buffer of the polyline or point.
     */
    @Override
    public void onMouseClicked(MouseEvent event) {

      super.onMouseClicked(event);

      if (startOver) {
        gLayer.removeAll();
        startOver = false;
      }

      // point clicked
      Point currPoint = jMap.toMapPoint(event.getX(), event.getY());
      System.out.println(event.getX()+" "+event.getY());
      // on right-click, compute the buffer
      if (event.getButton() == MouseEvent.BUTTON3) {
        if (polyLine.getSegmentCount() > 0) {
          // polyline
          bufferedArea = GeometryEngine.buffer(
              polyLine,
              jMap.getSpatialReference(),
              BUFFER_DISTANCE,
              jMap.getSpatialReference().getUnit());
        } else if (prevPoint != null) {
          // line
          bufferedArea = GeometryEngine.buffer(
              prevPoint,
              jMap.getSpatialReference(),
              BUFFER_DISTANCE,
              jMap.getSpatialReference().getUnit());
        } else {
          // point
          bufferedArea = GeometryEngine.buffer(
              currPoint,
              jMap.getSpatialReference(),
              BUFFER_DISTANCE,
              jMap.getSpatialReference().getUnit());
          Graphic currPointGraphic = new Graphic(currPoint, SYM_POINT);
          gLayer.addGraphic(currPointGraphic);
        }

        // add the buffered area to the graphics layer
        Graphic bufferedGraphic = new Graphic(bufferedArea, SYM_BUFFER);
        gLayer.addGraphic(bufferedGraphic);

        // start new on next click
        startOver = true;
        prevPoint = null;
        polyLine.setEmpty();

        return;
      }

      Graphic currPointGraphic = new Graphic(currPoint, SYM_POINT);
      gLayer.addGraphic(currPointGraphic);

      // on left-clicks, extend the current polyline
      if (prevPoint != null) {
        Line line = new Line();
        line.setStart(prevPoint);
        line.setEnd(currPoint);

        Segment segment = new Line();
        segment.setStart(prevPoint);
        segment.setEnd(currPoint);
        polyLine.addSegment(segment, false);

        Graphic lineGraphic = new Graphic(polyLine, SYM_LINE);
        gLayer.addGraphic(lineGraphic);
      }

      prevPoint = currPoint;
    }
  }