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
package org.gatein.api.rest.resource;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
@XmlRootElement(name = "link")
public class Link
{
   //this class basically just creates atom style links
   //TODO: use a custom implementation or use a atom library?
   //TODO: implement type, title, hreflang, length?
   
   protected String rel;
   protected String href;
   
   public Link()
   {
      
   }
   
   public Link(String relation, String href)
   {
      this.rel = relation;
      this.href = href;
   }

   @XmlAttribute(name = "rel")
   public String getRel()
   {
      return rel;
   }

   public void setRel(String rel)
   {
      this.rel = rel;
   }

   @XmlAttribute(name = "href")
   public String getHref()
   {
      return href;
   }

   public void setHref(String href)
   {
      this.href = href;
   }
   
   public boolean equals(Link link)
   {
      if (link.getHref().equals(this.getHref()) && (link.getRel().equals(this.getRel())))
      {
         return true;
      }
      else
      {
         return false;
      }
   }
}

