/*
 * Copyright (c) 2015. Konstantin Macarenco
 *
 * [This program is licensed under the GPL version 3 or later.]
 *
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 *
 */

package edu.pdx.konstan2.PortlandLive;

/**
 * Created by konstantin on 7/26/15.
 *
 * Class description
 *  In context of this application MasterTask is any activity that wants to communicate with
 *  the web API
 *
 *  Web API calls run() method upon return indicating that API call is finished.
 *
 */
public interface MasterTask {
    public void run(String methodName);
}
