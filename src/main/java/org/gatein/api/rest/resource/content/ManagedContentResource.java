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
package org.gatein.api.rest.resource.content;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.gatein.api.content.WSRP;
import org.gatein.api.rest.resource.IdentifiableResource;
import org.gatein.api.rest.resource.Link;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
@XmlRootElement(name = "managedcontent")
public class ManagedContentResource extends IdentifiableResource
{
   public static final String LINK_REL = "gatein.managedcontent";
   public enum queryProperties {NAME, DESCRIPTION, DISPLAYNAME, ID};
   
   protected String description;

   protected List<ManagedContentResource> managedContents;

   @XmlElement(name="description")
   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }
   
   @XmlTransient
   public Link getContentLink()
   {
      for (Link link : getLinks())
      {
         if (link.getRel().equals(PortletContentResource.LINK_REL))
         {
            return link;
         }
         else if (link.getRel().equals(GadgetContentResource.LINK_REL))
         {
            return link;
         }
         else if (link.getRel().equals(WSRPRepositoryResource.LINK_REL))
         {
            return link;
         }
      }
      return null;
   }
   
   public void setContentLink(Link contentLink)
   {
      links.add(contentLink);
   }
}

