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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.gatein.api.rest.resource.content.ApplicationResource;
import org.gatein.api.rest.resource.content.PortletContentResource;
import org.gatein.api.rest.resource.content.PortletRepositoryResource;
import org.gatein.api.rest.resource.handler.PortletRepositoryHandler;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
@Path("/api/contentregistry/contents/portlets")
public class PortletRespositoryService
{
   public static final String SERVICE_PATH="/api/contentregistry/contents/portlets";
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   public PortletRepositoryResource getPortletRepositoryResource(@Context UriInfo uriInfo)
   {
      PortletRepositoryHandler handler = new PortletRepositoryHandler(uriInfo);
      return handler.getApplications();
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{applicationId}")
   public ApplicationResource getApplication(@PathParam("applicationId") String applicationId,@Context UriInfo uriInfo)
   {
      PortletRepositoryHandler handler = new PortletRepositoryHandler(uriInfo);
      
      return handler.getApplication(applicationId);
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{applicationId}/{portletId}")
   public PortletContentResource getPortletContentResource(@PathParam("applicationId") String applicationId, @PathParam("portletId") String portletId, @Context UriInfo uriInfo)
   {
      PortletRepositoryHandler handler = new PortletRepositoryHandler(uriInfo);
      return handler.getPortlet(applicationId, portletId);
   }
   
   public PortletContentResource getPortletContentResource(String url, UriInfo uriInfo)
   {      
      System.out.println("ROOT : " +  uriInfo.getBaseUri().toString() + SERVICE_PATH);
      System.out.println("URL : " + url);
      int index = url.indexOf(uriInfo.getBaseUri().toString() + SERVICE_PATH);
      
      String tmpURL = url.substring((uriInfo.getBaseUri().toString() + SERVICE_PATH).length());
      
      String[] urlElement = tmpURL.split("/");
      System.out.println("TMPURL : " + tmpURL);
      
      PortletRepositoryHandler handler = new PortletRepositoryHandler(uriInfo);
      
      System.out.println("APPLICATION ID : " + urlElement[0]);
      System.out.println("PORTLET NAME : " + urlElement[1]);
      System.out.println("[2] : " + urlElement[2]);
      String applicationId = urlElement[1];
      String portletId = urlElement[2];
      
      return handler.getPortlet(applicationId, portletId);
   }
}

