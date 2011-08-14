/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2011, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.gatein.api.rest.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.gatein.api.rest.resource.content.GadgetContentResource;
import org.gatein.api.rest.resource.content.GadgetDataResource;
import org.gatein.api.rest.resource.content.GadgetRepositoryResource;
import org.gatein.api.rest.resource.handler.GadgetRepositoryHandler;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
@Path("/api/contentregistry/contents/gadgets")
public class GadgetRepositoryService
{
   public static final String SERVICE_PATH="/api/contentregistry/contents/gadgets";

   @GET
   @Produces(MediaType.APPLICATION_XML)
   public GadgetRepositoryResource getGadgetRepositoryResource(@Context UriInfo uriInfo)
   {
      GadgetRepositoryHandler handler = new GadgetRepositoryHandler(uriInfo);
      return handler.getGadgets();
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{gadgetId}")
   public GadgetContentResource getGadgetContentResource(@PathParam("gadgetId") String gadgetId, @Context UriInfo uriInfo)
   {
      GadgetRepositoryHandler handler = new GadgetRepositoryHandler(uriInfo);
      return handler.getGadget(gadgetId);
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{gadgetId}/content")
   public GadgetDataResource getGadgetDataContent(@PathParam("gadgetId") String gadgetId, @Context UriInfo uriInfo)
   {
      GadgetRepositoryHandler handler = new GadgetRepositoryHandler(uriInfo);
      return handler.getGadgetData(gadgetId);
   }
}

