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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.gatein.api.rest.resource.IdentifiableResource;

/**
 * @author <a href="mailto:mwringe@redhat.com">Matt Wringe</a>
 * @version $Revision$
 */
@XmlRootElement(name = "wsrp-portlet")
public class WSRPContentResource extends IdentifiableResource
{
   public static final String LINK_REL = "gatein.content.wsrp";
   
   protected String invokerName;
   protected String portletName;
   
   @XmlElement(name="invoker-name")
   public String getInvokerName()
   {
      return invokerName;
   }

   public void setInvokerName(String invokerName)
   {
      this.invokerName = invokerName;
   }
   
   @XmlElement(name="portlet-name")
   public String getPortletName()
   {
      return portletName;
   }

   public void setPortletName(String portletName)
   {
      this.portletName = portletName;
   }
}

