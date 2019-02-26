<%--
    Document   : index
    Created on : Mar 23, 2012, 11:42:02 AM
    Author     : akachukwu.ntukokwu
--%>

<%@page import="etz.com.appMonitor.controller.AppMonitorController"%>
<%@page import="java.util.List"%>
<%@page import="etz.com.appMonitor.controller.AppMonitorBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String i1 = request.getParameter("i1");
            String i2 = request.getParameter("i2");
            String order = request.getParameter("D1");
            String B1 = request.getParameter("B1");
            if(B1!=null)
                {
                session.setAttribute("order", order);
                }
            order =  ""+session.getAttribute("order");
            if(order==null)
                {
                order="1";
                }

            AppMonitorController ac = new AppMonitorController();

            List<AppMonitorBean> rcs = ac.getNodeStatus(order);


			String bname = "";
			
			
			
			try
			{
				com.etranzact.supportmanager.action.AuthenticationAction a = (com.etranzact.supportmanager.action.AuthenticationAction)request.getSession().getAttribute("authenticator");			
				out.println("a " + a.getCurUser().getUser_code());
				if(a.getCurUser().getUser_code().equals("033"))
				{
					bname = "UBA";
				}
				else if(a.getCurUser().getUser_code().equals("011"))
				{
					bname = "First";
				}
				else if(a.getCurUser().getUser_code().equals("032"))
				{
					bname = "Union";
				}
			}catch(Exception e)
			{
				out.println("a " + e.getMessage());
			}

%>

<html>

    <head>
        <meta http-equiv="Content-Language" content="en-us">
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <meta http-equiv="refresh" content="60">
        <title>etz-Application Monitor</title>

    </head>

    <body topmargin="0">

        <div align="center">
            <marquee style="font-family: Traditional Arabic; font-size: 11px; color: #FF0000" direction="right">eTranzact TMC node monitor</marquee><table border="0" width="90%" cellspacing="0" cellpadding="0">
				 <tr>
                    <td  align="center" colspan="18" width="100%" height="100%">
					<table border="0" width="100%" cellspacing="1" cellpadding="0">
					<td width="14%" bgcolor="#FF0000" align="center">
					<font style="font-size: 9pt; font-weight:700" face="Cambria" color="#FFFFFF">
					Total Unavailable IN Node</font></td>

					<td width="7%"><font face="Calibri">&nbsp;<%=ac.getDownInNode()%></font></td>

					<td width="13%" bgcolor="#008000">
					<font style="font-size: 9pt; font-weight:700" face="Cambria" color="#FFFFFF">
					Total Available IN Node</font></td>

					<td width="22%"><font face="Calibri">&nbsp;<%=ac.getUpInNode()%></font></td>

					<td width="22%">&nbsp;</td>

					<td width="22%" rowspan="2">
					<form method="POST" action="">
						<p>
						<select size="1" name="D1" style="font-size: ptpx; font-family: Shonar Bangla; border-style: solid; border-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px">
                                                    <option  value="1" <%if(order.equals("1")){out.print("selected");}%>>Out Going Node</option>
						<option value="2" <%if(order.equals("2")){out.print("selected");}%>>Incoming Node</option>
						</select><input type="submit" value="Set" name="B1" style="border-style: solid; border-width: 1px; padding-left: 4px; padding-right: 4px; padding-top: 1px; padding-bottom: 1px"></p>
					</form>
					</td>

					<tr>
					<td width="14%" bgcolor="#FF0000" align="center">
					<font style="font-size: 9pt; font-weight:700" face="Cambria" color="#FFFFFF">
					Total Unavailable OUT Node</font></td>

					<td width="7%"><font face="Calibri">&nbsp;<%=ac.getDownOutNode()%></font></td>

					<td width="13%" bgcolor="#008000">
					<font style="font-size: 9pt; font-weight:700" face="Cambria" color="#FFFFFF">
					Total Available OUT Node</font></td>

					<td width="22%"><font face="Calibri">&nbsp;<%=ac.getUpOutNode()%></font></td>

					<td width="22%">&nbsp;</td>

					</tr>
					<tr>
					<td colspan="6"><hr></td>

					</tr>

					</table>
					</td>




            </tr>
                <%
                   int position = 0;
                   for (int t = 0; t < rcs.size(); t++) 
                   {

                     try
                     {
                %>
                <tr>
                    <td width="155" align="center">
                    <p align="center">
                <% 
                     	if(rcs.get(position).getAppName().startsWith(bname))
                     	{
                   			if (rcs.get(position).getNodeStatus().equals("1")) 
                    		{
                           		out.print("<img border=\"0\" src=\"images/on_in.png\" width=\"41\" height=\"40\">");
                         	} 
                            else if (rcs.get(position).getNodeStatus().equals("2")) 
                          	{
                              	out.print("<img border=\"0\" src=\"images/non_in.png\" width=\"41\" height=\"40\">");
                          	}
	                        else
	                        {
	                         	out.print("<img border=\"0\" src=\"images/off_in.png\" width=\"41\" height=\"40\">");
	              		    }
                %>
                    </td>
                    <td width="44" align="center">
                        <p align="center">
                <%
	                	  	if (rcs.get(position).getEndpointStatus().equals("1")) 
	                	  	{
	                         out.print("<img border=\"0\" src=\"images/on_out.png\" width=\"41\" height=\"40\">");
	                    	} 
	                    	else if (rcs.get(position).getEndpointStatus().equals("2")) 
	                    	{
	                         out.print("<img border=\"0\" src=\"images/non_out.png\" width=\"41\" height=\"40\">");
	                     	}
	                     	else 
	                     	{
	                         out.print("<img border=\"0\" src=\"images/off_out.png\" width=\"41\" height=\"40\">");
	                 		}
                 %>

                    </td>
                    <td width="22" align="left" class="text"><%=rcs.get(position).getAppName()%></td>
                 <%
                         	position = position + 1;
                         }
                         
                     }
                     catch(Exception sd){}
                 %>

                </tr>

                <%
					}
                %>

            </table>
        </div>

    </body>

</html>