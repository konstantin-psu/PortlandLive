/*
 * Copyright (c) 2015. Konstantin Macarenco
 *
 * [This program is licensed under the GPL version 3 or later.]
 *
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 *
 */

package edu.pdx.konstan2.trimetlive;

/**
 * Created by kmacarenco on 7/21/15.
 *
 * Interface for any class that desires to communicate with a Web API
 * AsyncJob is expected to have urlStirng, and response String.
 * and also execute() method which is basically just a trigger that lets the Class know
 * that request has been completed
 *
 */
public interface AsyncJob {
    public String url();
    public void setResponse(String response);
    public void execute();

}
