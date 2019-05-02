<#include "init.ftl">

              
<head>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
<script src="https://www.google.com/recaptcha/api.js" async defer></script>
</head>
<form id="simpleForm" action="?" method="POST">-->
<body onload=grecaptcha.execute();>
<div class="g-recaptcha"
       data-sitekey="6LcyK3UUAAAAAH0RtvfcFiIRibdSVqqGfQG7ZHLx"
       data-callback="YourOnSubmitFn"
       data-size="invisible">
  </div>    

</form>

<div class="article-header" style="display: block;"> <h1 style="margin-top: 5px;">Create Your Password</h1> </div>

<p><strong>
<br>

Enter instruction text here .</strong>
</p>
<p><i>
<br>
<br>All fields are required.</i>
</p>

<form id="savepassword" name="savepassword" method="post"  >
<!--<input type="hidden" name="validateVisibleCheck" id="validateVisibleCheck" value="false" /> 
<input type="hidden" name="enableSubmitButton" id ="enableSubmitButton" value="false" /> 
<input type="hidden" name="hideErrorMessage" id="hideErrorMessage" value="false" />-->

<div class="function-boxes"> 
<label for="userName" class="personal-info-text">Username : youremail@youremail.com </label> 
<br>
<br>
<label for="newpassword" class="personal-info-text clear">Enter Password</label> 
<label class="hidden-off-screen"> 
<!--<p>Passwords are case-sensitive and must contain:</p>
<ul>
<li class="validationDefault">8 to 15 characters in length</li>
<li class="validationDefault">At least 1 uppercase letter</li>
<li class="validationDefault">At least 1 lowercase letter</li>
<li class="validationDefault">At least 1 number</li><li class="validationDefault">No special characters</li>
</ul> -->
</label>
 <div class="pharmacytextbox"> 
 <div class="main-div"> 
 <div class="inputField">
  <input type="text" id="tmpnewpassword" name="tmpnewpassword" value="" size="30" maxlength="15" validationfields="NotBlank,Password" namecheck="password" title="enter a password" data-toggle-password-from="newpassword" style="display: none;">
  <input type="password" id="newpassword" name="newpassword" value="" size="30" maxlength="15" validationfields="NotBlank,Password" namecheck="password" autocomplete="off" title="enter a password">
   </div> 
   <div class="displayValidationImage"> 
   <span class="defaultImage validationImage newpassword-indicator"></span> 
   </div> </div> 
   </div>
    <div id="newPassword-error-message"> </div> 
    <div class="clear"> 
    <div class="icheckbox_minimal-black" style="position: relative;">
    <input type="checkbox" id="showPassword" style="position: absolute; opacity: 0;">
   <ins class="iCheck-helper" style="position: absolute;top: 0%;left: 0%;display: block;width: 100%;height: 100%;margin: 0px;padding: 0px;background: rgb(255, 255, 255);border: 0px;opacity: 0;"></ins>
    </div> 
    <span><label class="showPasswordLabel label-checkbox label-icheck" for="showPassword"><strong>Show password when typing</strong> (optional)</label></span> 
    </div> 
    <div id="password-condition-message" class="box-inner-content">
				<p>Passwords are case-sensitive and must contain:</p>
							<ul class="password-inline-validations">
								<li id="password-validation-RequiredLength" class="validationDefault">8 to 15 characters in length</li>
								<li id="password-validation-RequiredUpperCase" class="validationDefault">At least 1 uppercase letter</li>
								<li id="password-validation-RequiredLowerCase" class="validationDefault">At least 1 lowercase letter</li>
								<li id="password-validation-RequiredNumber" class="validationDefault">At least 1 number</li>
								<li id="password-validation-RequiredNoSpecialCharacter" class="validationDefault">No special characters</li>
								
							</ul>
						</div>					
   
</div>
<button validationforinlinesubmit="disable-submit-button" type="submit" class="red-button red-button-thick" disablebutton="true" style="background: none 0px 0px repeat scroll rgb(217, 217, 217); color: rgb(153, 153, 153); cursor: default;">Save Password</button>
</form>

<!--
<#if validGuid?exists && validGuid=="true">
	<form class="loyalty-page-form signup-wrapper ng-cloak" 
		ng-controller="loyalty-page" data-ng-submit="signup()" data-ng-init="
			form = {
				userName: '${(userName!)?js_string?xml}',				
				returnToUrl: '${(returnToUrl!)?js_string?xml}'
			};
			signupUrl = '${properties.getProperty('riteaid.properties', 'webservice.signup', '')?js_string?xml}';
		" novalidate>
		<#-- ----------------------Initial identification---------------------- -->
	<!--	<div class="login-header">-->
   <!--	 <div class="hidden-md hidden-lg login-toggle-header"><span tabindex="0">-->
		<!--			${messages["login.header"]!}</span></div> 
				<div class="hidden-xs hidden-sm">${messages["login.header"]!}</div>
			</div>
			
		<#if lastUpdateBy?exists && lastUpdateBy == "OLLOY">
						
				<div class="input-overlay">
					<label>
							${messages["login.username.name"]!} : ${userName}
						 </label>			
					</div>	
					
					<div class="input-overlay">
					<ra-validation-input data-ra-attr-type="{{ showPassword ? 'text' : 'password' }}"  
						field-name="${(messages["signup.password.name"]!)?html}" data-ra-attr-autocomplete="new-password"
						ra-validate="NotBlank,MinLength,Password" data-ra-model="form.password" min-length="8"
						ra-readonly="processing" data-ra-attr-maxlength="15"></ra-validation-input>
					<ra-tooltip2>                        
						${messages["signup.password.tooltip"]!}
					</ra-tooltip2>
					<ra-validation-message data-ra-model="form.password"></ra-validation-message>
				</div>
				
				<div class="input-overlay">
					<label>
						<input type="checkbox" data-ng-model="showPassword" data-pretty-check="" />
						${messages["signup.show-password"]!}
					</label>
				</div>	
				
					<div class="next-button"> 
						<input id="submitBtn" data-ng-disabled="processing" class="red-button2 next-button" value="Submit"> 
					</div>
		
		<#else>
			
		
			
			<div data-ng-show="!processing" class="form-container">
				<#if !signedIn>
					<div class="input-overlay">
						${messages['already.active.account']}									
					</div>		
				
					<div class="next-button"> 
						<input id="loginBtn" data-ng-disabled="processing" class="red-button2 next-button" value="Log In"> 
					</div>	
				</#if>	
			</div>
		</#if>	
	</form>
<#else>
    <div class="signup-wrapper loyalty-page-form-off"> 
		<div class="login-header">
			<div class="hidden-md hidden-lg login-toggle-header"><span tabindex="0">
				${messages["signup.new-header"]!}</span></div>
			<div class="hidden-xs hidden-sm">${messages["signup.new-header"]!}</div>
		</div>
		<p>${linkExpiredMessage}</p>
		
		<div class="next-button"> 
			<input id="signUpBtn" data-ng-disabled="processing" class="red-button2 next-button" value="Sign Up"> 
		</div>
	</div> -->
 <!--</#if> -->

<!--<#include "login_and_signup_modal.ftl"> -->