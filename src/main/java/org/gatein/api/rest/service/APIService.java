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

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.gatein.api.rest.resource.APIRootResource;
import org.gatein.api.rest.resource.DocumentationResource;
import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.PagesResource;
import org.gatein.api.rest.resource.SitesResource;
import org.gatein.api.rest.resource.content.ContentRegistryResource;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
@Path("/api")
public class APIService
{

   public APIService()
   {
      System.out.println("API REST SERVICE STARTED");
   }
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   public APIRootResource getAPIRootResource(@Context UriInfo uriInfo)
   {
      return generateRootResource(uriInfo);
   }
   
   protected APIRootResource generateRootResource(UriInfo uriInfo)
   {
      APIRootResource rootResource = new APIRootResource();
      
      //add 'self' link
      rootResource.setSelfLink(createSelfLink(uriInfo.getRequestUri()));
      
      rootResource.getLinks().add(createLink(uriInfo.getBaseUriBuilder(), DocumentationResource.LINK_REL, DocumentationService.SERVICE_PATH));
      rootResource.getLinks().add(createLink(uriInfo.getBaseUriBuilder(), PagesResource.LINK_REL, PagesService.SERVICE_PATH));
      rootResource.getLinks().add(createLink(uriInfo.getBaseUriBuilder(), ContentRegistryResource.LINK_REL, ContentRegistryService.SERVICE_PATH));
      rootResource.getLinks().add(createLink(uriInfo.getBaseUriBuilder(), SitesResource.LINK_REL, SitesService.SERVICE_PATH));
      
      return rootResource;
   }
   
   
   protected Link createLink(UriBuilder uriBuilder, String linkRel, String servicePath)
   {
      URI linkURI = uriBuilder.clone().path(servicePath).build();
      return new Link(linkRel, linkURI.toString());
   }
   
   protected Link createSelfLink(URI linkURI)
   {
      return new Link("self", linkURI.toString());
   }
}

