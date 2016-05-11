/*
 * Licensed to GraphHopper GmbH under one or more contributor
 * license agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * GraphHopper GmbH licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.graphhopper.jsprit.core.problem.solution.route.activity;

import com.graphhopper.jsprit.core.problem.AbstractActivity;
import com.graphhopper.jsprit.core.problem.Capacity;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Shipment;

public final class PickupShipment extends AbstractActivity implements PickupActivity{

    private Shipment shipment;

    private double endTime;

    private double arrTime;

    public double readyTime;

    private double earliest = 0;

    private double latest = Double.MAX_VALUE;

    private double softEarliest = 0.;

    private double softLatest = Double.MAX_VALUE;

    private double setup = 0;

    public PickupShipment(Shipment shipment) {
        super();
        this.shipment = shipment;
        this.setup = shipment.getPickupSetupTime();
    }

    private PickupShipment(PickupShipment pickupShipmentActivity) {
        this.shipment = (Shipment) pickupShipmentActivity.getJob();
        this.arrTime = pickupShipmentActivity.getArrTime();
        this.readyTime = pickupShipmentActivity.getReadyTime();
        this.endTime = pickupShipmentActivity.getEndTime();
        setIndex(pickupShipmentActivity.getIndex());
        this.earliest = pickupShipmentActivity.getTheoreticalEarliestOperationStartTime();
        this.latest = pickupShipmentActivity.getTheoreticalLatestOperationStartTime();
        this.softEarliest = pickupShipmentActivity.getSoftLowerBoundOperationStartTime();
        this.softLatest = pickupShipmentActivity.getSoftUpperBoundOperationStartTime();
        this.setup = pickupShipmentActivity.getSetupTime();
    }

    @Override
    public Job getJob() {
        return shipment;
    }

    @Override
    public void setTheoreticalEarliestOperationStartTime(double earliest) {
        this.earliest = earliest;
        if(this.softEarliest < earliest)
            this.softEarliest = earliest;
    }

    @Override
    public void setTheoreticalLatestOperationStartTime(double latest) {
        this.latest = latest;
        if(this.softLatest > latest)
            this.softLatest = latest;
    }

    public void setSoftEarliestoperationStartTime(double earliest) {
        this.softEarliest = earliest;
        if(this.earliest > earliest)
            this.earliest = earliest;
    }

    public void setSoftLatestOperationStartTime(double latest) {
        this.softLatest = latest;
        if(this.latest < latest)
            this.latest = latest;
    }

    public void setSetupTime(double setup) {
    	this.setup = setup;
    }

    @Override
    public String getName() {
        return "pickupShipment";
    }

    @Override
    public Location getLocation() {
        return shipment.getPickupLocation();
    }

    @Override
    public double getTheoreticalEarliestOperationStartTime() {
        return earliest;
    }

    @Override
    public double getTheoreticalLatestOperationStartTime() {
        return latest;
    }

    public double getSetupTime() {
    	return setup;
    }

    @Override
    public double getOperationTime() {
        return shipment.getPickupServiceTime();
    }

    @Override
    public double getArrTime() {
        return arrTime;
    }

    @Override
    public double getEndTime() {
        return endTime;
    }

    @Override
    public void setArrTime(double arrTime) {
        this.arrTime = arrTime;
    }

    @Override
    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    @Override
    public TourActivity duplicate() {
        return new PickupShipment(this);
    }

    public String toString() {
        return "[type=" + getName() + "][locationId=" + getLocation().getId()
            + "][size=" + getSize().toString()
            + "][twStart=" + Activities.round(getTheoreticalEarliestOperationStartTime())
            + "][twEnd=" + Activities.round(getTheoreticalLatestOperationStartTime())
            + "][Setup=" + Activities.round(getSetupTime()) + "]";
    }

    @Override
    public Capacity getSize() {
        return shipment.getSize();
    }

	@Override
	public double getSoftLowerBoundOperationStartTime() {
        return softEarliest;
	}

    @Override
    public double getReadyTime() {
        return readyTime;
    }

    @Override
    public void setReadyTime(double readyTime) {
        this.readyTime = readyTime;
    }

	@Override
	public double getSoftUpperBoundOperationStartTime() {
        return softLatest;
	}
}
