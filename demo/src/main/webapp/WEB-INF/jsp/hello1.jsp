<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 

<portlet:defineObjects/>

<div id="hello-portlet-wrapper">
<p>
Hello, world portlet.
</p>

<c:if test="${! empty param.greeting}">
<p>
greeting: ${param.greeting}
</p>
<c:remove var="param.greeting"/>
</c:if>

<form action="<portlet:actionURL name="greetAction"></portlet:actionURL>" method="POST">
Name:<br/>
<input type="text" name="name"/>
<input type="submit"/>
</form>

</div>