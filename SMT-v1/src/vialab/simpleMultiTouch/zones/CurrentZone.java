package vialab.simpleMultiTouch.zones;

import java.util.ArrayList;
import java.util.Vector;

import org.jdesktop.animation.timing.Animator;

import vialab.simpleMultiTouch.TouchClient;


public class CurrentZone extends PolyZone{
	ArrayList<Zone> zones = new ArrayList<Zone>();
	ArrayList<RectZone> cZones = new ArrayList<RectZone>();
	ArrayList<Boolean[]> starting = new ArrayList<Boolean[]>();


	float[] controlPoints;
	float eWidth;
	float eHeight;
	int sizeTime;
	float totalDist = 0;

	boolean ready = false;

	public CurrentZone(float[] coordinates, boolean curved, float[] controlPoints, float eWidth, float eHeight, int time) {
		super(coordinates, eWidth, eHeight, curved);
		this.controlPoints = controlPoints;
		this.eWidth = eWidth;
		this.eHeight = eHeight;
		this.sizeTime = time;

		createControlZones();
		findTotalDistance();
		ready = true;
	}

	public ArrayList<Zone> getZones(){
		return zones;
	}
	
	public void findTotalDistance(){
		float totalX = 0;
		float totalY = 0;

		for(int j = 0; j < controlPoints.length-3; j = j + 2){
			totalX += Math.sqrt((controlPoints[j]-controlPoints[j+2])*(controlPoints[j]-controlPoints[j+2]));
			totalY += Math.sqrt((controlPoints[j+1]-controlPoints[j+3])*(controlPoints[j+1]-controlPoints[j+3]));
		}
		

		totalDist = (float) Math.sqrt((totalX+totalY)*(totalX+totalY));
	}

	public void createControlZones(){
		int size = 10;
		for(int j = 0; j < controlPoints.length; j = j + 2){
			RectZone rect = new RectZone(controlPoints[j]-size/2, controlPoints[j+1]-size/2, size, size);
			cZones.add(rect);
		}
	}
	public float findDistance(float x1, float y1, float x2, float y2){
		float xDist = x2-x1;
		float yDist =  y2-y1;

		return (float) Math.sqrt(xDist*xDist + yDist*yDist);

	}

	// Minimum Distance between a Point and a Line - http://paulbourke.net/geometry/pointline/
	// xT and yT is the point to check
	// lx1 and ly1 is an endpoint of the line
	// lx2 and ly2 is another endpoint of the line
	public float minDistPointNLine(int xT, int yT, int lx1, int ly1, int lx2, int ly2){
		float u = (xT- lx1)*(lx2-lx1) + (yT- ly1)*(ly2-ly1);
		float u2 = (( lx2- lx1)*( lx2-lx1) + (ly2-ly1)*(ly2-ly1));
		u = u/u2;
		float x2 = lx1 + u *(lx2-lx1);
		float y2 = ly1 + u *(ly2-ly1);
		return findDistance(xT, yT, x2, y2);
	}

	public void drawZone(){
		super.drawZone();
		if(ready){
			//synchronized(TouchClient.zoneList){
			Vector<Zone> list = TouchClient.getZoneList();


			for(int i = 0; i < list.size(); i++){
				Zone zone = list.get(i);
				if(zone != this && zone.isControllable() && zone.active){
					float xT = zone.getXTimesMatrix() + zone.getWidth()/2;
					float yT = zone.getYTimesMatrix() + zone.getHeight()/2;


					if(zone.controlled && zone.getNumIds() != 0) {
						resetControl(zone);
					} else if(this.contains(xT, yT)){

						if(zone.getNumIds() == 0){
							if(!zone.controlled){
								controlZone(zone, xT, yT);
							} else {
								int index = zones.indexOf(zone);
								if(index < 0){
									continue;
								}
								int index2 = 0;

								for(int k = 0; k < cZones.size(); k++){
									RectZone rect = cZones.get(k);

									if(!starting.get(index)[index2] && rect.contains(xT, yT)){
										if(index2 == cZones.size()-1){
											resetControl(zone);
											endOfFlow(zone);
										} else {
											resetControl(zone);
											sendToNext(zone, xT, yT, cZones.get(k+1));
										}
										break;
									}
									index2++;
								}
							}

						} 

					} else if(zone.controlled){
						resetControl(zone);
					}

				}

			}
			//}
		}
	}

	public void action(Zone zone, float xT, float yT, float distFinalX, float distFinalY){
		int timeX = (int) (Math.abs((distFinalX/totalDist)*sizeTime));
		if(timeX == 0){
			timeX = 1;
		}

		int timeY = (int) (Math.abs((distFinalY/totalDist)*sizeTime));
		if(timeY == 0){
			timeY = 1;
		}

		int theTime = Math.max(timeX, timeY);
		//System.out.println(theTime + " seconds");

		zone.zoneAnimator = new Animator(theTime, zone);

		/*if(Math.abs(distFinalX) > Math.abs(distFinalY)){
			zone.setStartx(zone.getX());
			zone.setEndx(zone.getX() + distFinalX);
			zone.setStarty(zone.getY());
			zone.setEndy(zone.getY());
			System.out.println("X: " +distFinalX);

		} else {
			zone.setStartx(zone.getX());
			zone.setEndx(zone.getX());
			zone.setStarty(zone.getY());
			zone.setEndy(zone.getY() + distFinalY);
			System.out.println("Y: " + distFinalY);

		}*/
		zone.setStartx(zone.getX());
		zone.setEndx(zone.getX() + distFinalX);
		zone.setStarty(zone.getY());
		zone.setEndy(zone.getY() + distFinalY);
		zones.add(zone);

		zone.controlled = true;
		Boolean[] startedInCZone = new Boolean[cZones.size()];
		int index = 0;

		for(RectZone rect: cZones){
			if(rect.contains(xT, yT)){
				startedInCZone[index] = true;

			} else {
				startedInCZone[index] = false;
			}
			index++;
		}
		starting.add(startedInCZone);
		zone.zoneAnimator.setResolution(5);
		zone.zoneAnimator.start();
	}

	public void sendToNext(Zone zone, float xT, float yT, RectZone r){

		float distFinalX = r.getX()+r.getWidth()/2-xT;
		float distFinalY = r.getY()+r.getHeight()/2-yT;

		action(zone, xT, yT, distFinalX, distFinalY);

	}
	public void resetControl(Zone zone){

		int index = zones.indexOf(zone);
		if(index >= 0){
			zone.controlled = false;
			starting.remove(index);
			zones.remove(zone);

			zone.zoneAnimator.stop();
			zone.zoneAnimator = null;
		}
	}

	public void controlZone(Zone zone, float xT, float yT){

		int closestLine = -1;
		float closestDistLine = Integer.MAX_VALUE;

		for(int j = 0; j < controlPoints.length-2; j = j + 2){


			float u = (xT- controlPoints[j])*(controlPoints[j+2]-controlPoints[j]) + (yT- controlPoints[j+1])*(controlPoints[j+3]-controlPoints[j+1]);
			float u2 = ((controlPoints[j+2]-controlPoints[j])*(controlPoints[j+2]-controlPoints[j]) + (controlPoints[j+3]-controlPoints[j+1])*(controlPoints[j+3]-controlPoints[j+1]));
			if(u2 < 0){
				u2 = -u2;
			}

			u = u/u2;

			float x2 = controlPoints[j] + u *(controlPoints[j+2]-controlPoints[j]);
			float y2 = controlPoints[j+1] + u *(controlPoints[j+3]-controlPoints[j+1]);
			float ddiisstt = findDistance(xT, yT, x2, y2);

			if(ddiisstt  < closestDistLine){

				closestDistLine = ddiisstt;
				closestLine = j;
			}
		}

		int two = closestLine + 2;


		float distFinalX = controlPoints[two]-xT;
		float distFinalY = controlPoints[two+1]-yT;

		zone.setWidth(eWidth);
		zone.setHeight(eHeight);

		zone.resetMatrix();
		if(controlPoints[closestLine]-controlPoints[two] != 0){

			zone.setX(xT - zone.getWidth()/2);
			zone.setY(controlPoints[two+1] - eHeight/2);

			distFinalY = 0;
		} else if(controlPoints[closestLine+1]-controlPoints[two+1] != 0){
			zone.setX(controlPoints[two] - eWidth/2);
			zone.setY(yT - zone.getHeight()/2);

			distFinalX = 0;
		}



		action(zone, xT, yT, distFinalX, distFinalY);

	}

	public void endOfFlow(Zone zone){

	}
}
