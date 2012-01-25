<%--
 Copyright 2011 Ixonos Plc, Finland. All rights reserved.

 You should have received a copy of the license text along with this program.
 If not, please contact the copyright holder (http://www.ixonos.com/).
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>

<%@ page import="javax.portlet.PortletPreferences" %>
<%@ page import="javax.portlet.WindowState" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ page contentType="text/html" isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<portlet:defineObjects />


<portlet:actionURL var="saveTestUrl">
	<portlet:param name="action" value="setText" />
</portlet:actionURL>


<div class="portlet-section-body">

		<h1 class="portlet-section-header">
			Encoding test
		</h1>
        <div class="search">
		<form:form name="testForm" commandName="test" method="post"
			action="${saveTestUrl}">

            <span class="portlet-form-field-label"> 
                Set some text with special characters (ä,ö,å etc): 
            </span>
            
			<span class="portlet-form-field"> 
			 <form:input class="defaultText"  path="text" /> 
			 <input type="submit" class="portlet-form-button" value="save"> 
			</span>

			
		</form:form>
		</div>
		<br></br>
		<div class="collection">
			<c:if test="${not empty test}">			
				<h1 class="portlet-section-header">
					Encoding test result:
				</h1>
				<span>
						<c:out value="${test.text }"/>
				</span>
			</c:if>
			
		</div>
	</div>

