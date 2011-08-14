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

import org.gatein.api.rest.resource.PageResource;
import org.gatein.api.rest.resource.PagesResource;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
@Path("/api/pages")
public class PagesService
{
   public static final String SERVICE_PATH = "/api/pages";

   @GET
   @Produces(MediaType.APPLICATION_XML)
   public PagesResource getPages(@Context UriInfo uriInfo)
   {
      return generatePagesResource(uriInfo);
   }

   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{siteType}")
   public PagesResource getPages(@PathParam("siteType") String siteType, @Context UriInfo uriInfo)
   {
      System.out.println("SITE TYPE: " + siteType);
      return new PagesResource();
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{siteType}/{siteID}/{pageName}")
   public PagesResource getPages(@PathParam("siteType") String siteType, @PathParam("siteID") String siteId, @PathParam("pageName") String pageName, @Context UriInfo uriInfo)
   {
      System.out.println("SITE TYPE: " + siteType + " : PAGE NAME : " + pageName);
      return new PagesResource();
   }

   protected PagesResource generatePagesResource(UriInfo uriInfo)
   {
      return null;
//      Query query = PageTransformation.createQuery(uriInfo);
//      List<PageResource> pageResources = PagesTransformation.getPages(query);
//
//      PagesResource pagesResource = new PagesResource();
//      pagesResource.getPages().addAll(pageResources);
//
//      return pagesResource;
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   @Path("/{portalName}/{siteType}/{siteID}/{pageName}")
   public PageResource getPage(@PathParam("portalName") String portalName, @PathParam("siteType") String siteType, @PathParam("siteID") String siteId, @PathParam("pageName") String pageName, @Context UriInfo uriInfo)
   {
      return null;
      //return PagesTransformation.getPage(portalName, siteType, siteId, pageName);
   }
}
