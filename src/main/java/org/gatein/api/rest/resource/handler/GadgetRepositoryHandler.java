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

import org.gatein.api.content.Content;
import org.gatein.api.content.Gadget;
import org.gatein.api.content.Gadget.LocalData;
import org.gatein.api.portal.Portal;
import org.gatein.api.rest.resource.content.GadgetContentResource;
import org.gatein.api.rest.resource.content.GadgetDataResource;
import org.gatein.api.rest.resource.content.GadgetRepositoryResource;
import org.gatein.api.util.IterableIdentifiableCollection;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
public class GadgetRepositoryHandler extends Handler
{
   protected UriInfo uriInfo;
   
   public GadgetRepositoryHandler(UriInfo uriInfo)
   {
      this.uriInfo = uriInfo;
   }
   
   public GadgetRepositoryResource getGadgets()
   {
      GadgetRepositoryResource resource = new GadgetRepositoryResource();
      
      GadgetHandler gadgetHandler = new GadgetHandler(uriInfo);
      
      Portal portal = gateIn.getDefaultPortal();
      IterableIdentifiableCollection<Content> contents = portal.getContentRegistry().getAll();
      
      while (contents.iterator().hasNext())
      {
         Content content = null;
         content = contents.iterator().next();
         if (content != null && content.getType().getName().equals("gadget"))
         {
            //System.out.println("+++ IS A GADGET : " + content.getDisplayName());
            if (content instanceof Gadget)
            {
               resource.getGadgets().add(gadgetHandler.toGadgetResource((Gadget)content));
            }
         }
         else if (content != null)
         {
            //System.out.println("--- IS NOT A GADGET : " + content.getDisplayName());
         }
         else
         {
            //System.out.println("??? Something isn't right...");
         }
      }
      
      return resource;
   }
   
   public GadgetContentResource getGadget(String gadgetId)
   {
      GadgetHandler gadgetHandler = new GadgetHandler(uriInfo);
      
      Portal portal = gateIn.getDefaultPortal();
      
      Gadget gadget = portal.getContentRegistry().get(gateIn.gadgetId(gadgetId));
      
      return gadgetHandler.toGadgetResource(gadget);
   }

   public GadgetDataResource getGadgetData(String gadgetId)
   {
      GadgetHandler gadgetHandler = new GadgetHandler(uriInfo);
      
      Portal portal = gateIn.getDefaultPortal();
      
      Gadget gadget = portal.getContentRegistry().get(gateIn.gadgetId(gadgetId));
      
      if (gadget.isLocal() && gadget.getData() instanceof LocalData)
      {
         return gadgetHandler.toGadgetData((LocalData)gadget.getData());
      }
      else
      {
         return null;
      }
   }

}

