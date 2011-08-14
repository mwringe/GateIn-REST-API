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
package org.gatein.api.rest.resource.handler;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.CategoriesResource;
import org.gatein.api.rest.resource.content.ContentRegistryResource;
import org.gatein.api.rest.resource.content.ContentsResource;
import org.gatein.api.rest.service.CategoriesService;
import org.gatein.api.rest.service.ContentRegistryService;
import org.gatein.api.rest.service.ContentsService;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class ContentRegistryHandler extends Handler
{

   public ContentRegistryHandler(UriInfo uriInfo)
   {
      this.uriInfo = uriInfo;
   }

   public ContentRegistryResource getContentRegistryResource()
   {
      ContentRegistryResource rootResource = new ContentRegistryResource();

      //add the 'self' navigation link
      String href = uriInfo.getBaseUriBuilder().clone().path(ContentRegistryService.SERVICE_PATH).build().toString();
      rootResource.setSelfLink(new Link("self", uriInfo.getRequestUri().toString()));


      rootResource.setCategoriesLink(createLink(uriInfo.getBaseUriBuilder(), CategoriesResource.LINK_REL, CategoriesService.SERVICE_PATH));
      rootResource.setContents(createLink(uriInfo.getBaseUriBuilder(), ContentsResource.LINK_REL, ContentsService.SERVICE_PATH));
      
      return rootResource;
   }

   protected Link createLink(UriBuilder uriBuilder, String linkRel, String servicePath)
   {
      URI linkURI = uriBuilder.clone().path(servicePath).build();
      return new Link(linkRel, linkURI.toString());
   }


}

