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

import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.ContentsResource;
import org.gatein.api.rest.resource.content.GadgetRepositoryResource;
import org.gatein.api.rest.resource.content.PortletRepositoryResource;
import org.gatein.api.rest.resource.content.WSRPRepositoryResource;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
@Path("/api/contentregistry/contents")
public class ContentsService
{
   public static final String SERVICE_PATH="/api/contentregistry/contents";
   
   @GET
   @Produces(MediaType.APPLICATION_XML)
   public ContentsResource getContentResource(@Context UriInfo uriInfo)
   {
     ContentsResource resource = new ContentsResource();
     
     //add 'self' link
     resource.setSelfLink(createSelfLink(uriInfo.getRequestUri()));
     
     //add links for the various content types
     resource.getLinks().add(createLink(uriInfo.getBaseUriBuilder(), PortletRepositoryResource.LINK_REL, SERVICE_PATH + "/portlet"));
     resource.getLinks().add(createLink(uriInfo.getBaseUriBuilder(), WSRPRepositoryResource.LINK_REL, SERVICE_PATH + "/wsrp"));
     resource.getLinks().add(createLink(uriInfo.getBaseUriBuilder(), GadgetRepositoryResource.LINK_REL, GadgetRepositoryService.SERVICE_PATH));
     
     return resource;
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

