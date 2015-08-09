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
 */
public interface AsyncJob {
    public String url();
    public void setResponse(String response);
    public void execute();

}
