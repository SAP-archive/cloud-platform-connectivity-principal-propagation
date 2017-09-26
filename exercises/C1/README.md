# Exercise C1: Debugging in the Cloud Connector

#### Objective
In this exercise, you will learn how to debug a non-working cloud application on the Cloud Connector side. You will first have a look at the audit logs and then at the traces files.

#### Estimated time
10 minutes
<br /><br />

## Step-by-step guide
1. First let call again the cloud application and see the error message. If you close the application in the browser already, you can reopen it by calling the following URL:<br />
    https://a3demoprincipalpxxxxxxxxxxtrial.hanatrial.ondemand.com/A3_DemoPrincipalPropagation/?destination=ABAP_SYSTEM_PP<br />
    The error message tells you that the **Logon failed**, but you can't get more info here in the cloud. So, let's first check in the Cloud Connector.<br /><br />
    ![](../../images/c1-error-message.png)

1. Open the Cloud Connector and go to **Audits**. Then click on the **Edit** icon to change the trace level.<br /><br />
    ![](../../images/c1-scc-audit.png)

1. Change the **Subaccount Audit Level** from `Security` to `All` in order to see the cloud requests going through the Cloud Connector. Then Click **Save**.<br /><br />
    ![](../../images/c1-scc-audit-change.png)

1. Go to the browser and click the **Refresh** icon to send again the request from the cloud application.<br /><br />
    ![](../../images/c1-refresh-app.png)

1. Go back to the Cloud connector and refresh the audit logs by pressing the button **Go** and verify the access to the resources is allowed.<br /><br />
    ![](../../images/c1-scc-audit-check.png)

1. Everything seems to be fine in the audit logs, so let check the traces. Click on **Logs And Trace Files** and press the **Edit** button to change the trace level.<br /><br />
    ![](../../images/c1-log-edit.png)

1. Set the level of **Cloud Connector Loggers** from `Information` to to `Debug`.<br /><br />
    ![](../../images/c1-log-debug.png)

1. Go to the browser and click the **Refresh** icon to send again the request from the cloud application.<br /><br />
<br /><br />
    ![](../../images/c1-refresh-app.png)

1. Go back to the Cloud Connector and click again the **Edit** button to change the **Cloud Connectors Loggers** level.<br /><br />
    ![](../../images/c1-log-edit-02.png)

1. Set the level back to `Information`. This is not a mandatory step, but it makes easier to read later the traces later on after downloading them, because there is no waste after reproducing the error situation ;)<br /><br />
    ![](../../images/c1-log-information.png)

1. Now you can download the traces by clicking the **Download** icon of the file called **ljs.trace.log**.<br /><br />
    ![](../../images/c1-log-download.png)

1. Open the folder **Downloads** and extract the zip file called **scclogs.zip**.<br /><br />
        ![](../../images/c1-log-extract.png)

1. Open the extracted file called **ljs.trace.log** in Notepad++.<br /><br />
    ![](../../images/c1-log-notepad.png)

1. Let take a second to analyze the results:<br /><br />
    ![](../../images/c1-log-details.png)<br />
    - First you can see that the connection is established with the virtual host of the Access Control.
    - Then the Cloud Connector generates the X.509 certificate for the authentication in the ABAP system.
    - You can even see the subject of the certificate: **CN=pXXXXXXXXXX,Email=cpl360-XXX&teched.cloud.sap,O=SAP,C=DE**.
    - Access to the ICF service of the backend is also granted.
    - Finally you can see the certificate itself in the base64-encoded representation used in the HTTP header.
    Hence, we can be sure that the problem is not orginating from the Cloud Connector side.
