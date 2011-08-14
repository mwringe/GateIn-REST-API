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

import javax.ws.rs.core.UriInfo;

import org.gatein.api.content.Gadget;
import org.gatein.api.content.Gadget.LocalData;
import org.gatein.api.content.Gadget.RemoteData;
import org.gatein.api.rest.resource.Link;
import org.gatein.api.rest.resource.content.GadgetContentResource;
import org.gatein.api.rest.resource.content.GadgetDataResource;
import org.gatein.api.rest.service.GadgetRepositoryService;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class GadgetHandler extends Handler
{

   UriInfo uriInfo;
   
   public GadgetHandler(UriInfo uriInfo)
   {
      this.uriInfo = uriInfo;
   }
   
   public GadgetContentResource toGadgetResource(Gadget gadget)
   {
      GadgetContentResource gadgetResource = new GadgetContentResource();
      
      gadgetResource.setName(gadget.getName());
      gadgetResource.setDisplayName(gadget.getDisplayName());
      gadgetResource.setId(gadget.getId().toString());
      gadgetResource.setReferenceURI(gadget.getReferenceURI().toString());
      gadgetResource.setURI(gadget.getURI().toString());
      
      if (gadget.isLocal() && gadget.getData() instanceof LocalData)
      {
         String href = uriInfo.getBaseUriBuilder().clone().path(GadgetRepositoryService.SERVICE_PATH).path(gadget.getId().toString()).path("content").build().toString();
         Link dataLink = new Link (GadgetDataResource.LINK_REL, href);
         gadgetResource.setDataLink(dataLink);
      }
      
      if (gadget.getData() instanceof LocalData)
      {
        // System.out.println("LOCAL DATA : " + ((LocalData)gadget.getData()).getSource());
      }
      else
      {
         RemoteData remoteData = (RemoteData)gadget.getData();
        // System.out.println("REMOTE DATA : " + remoteData.getURI());
      }
      
      return gadgetResource;
   }

   public GadgetDataResource toGadgetData(LocalData data)
   {
      GadgetDataResource gadgetData = new GadgetDataResource();
      gadgetData.setData(data.getSource());
      return gadgetData;
   }

}

