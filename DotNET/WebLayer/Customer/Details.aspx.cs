using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Net.Mail;

using Pharmacy.BusinessLayer.Logic;

namespace WebLayer.Customer

{
    public partial class Details : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }
        
        protected void Update_Command(object sender, EventArgs e)
        {
            //Text='<%# String.Format("{0:dd.MM.yyyy}", DateTime.Now) %>'

            TextBox birthDateString = CustomerDetailsView.FindControl("BirthDateBox") as TextBox;
            DateTime birthDate = Util.ParseDate(birthDateString.Text);

            TextBox telephoneNumberString = CustomerDetailsView.FindControl("TelephoneNumber") as TextBox;
            String telephoneNumber = telephoneNumberString.Text;

            TextBox addressString = CustomerDetailsView.FindControl("Address") as TextBox;
            String address = addressString.Text;

            TextBox emailString = CustomerDetailsView.FindControl("Email") as TextBox;
            String email = emailString.Text;

            Pharmacy.BusinessLayer.Data.Customer customer = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"]));
            Pharmacy.BusinessLayer.Logic.CustomerService.UpdateCustomer(customer, telephoneNumber, address, email, birthDate);
        }

    protected void SendMailBtnException_Click(object sender, EventArgs e) {
                
            String receiver = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Email;
            String recipientName = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Name;
            String recipientEmail = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Email;

                MailMessage mailMessage = new MailMessage("pharmacy04@web.de", receiver);
                mailMessage.Subject = "C# Pharmacy04 - Prescription Bill";
                mailMessage.Body = "Dear " + recipientName + ", \n\n Your overall prescription bill is: XXX" + "€." +
                    "\n\n Below you can check your prescription details: "
                    + "\n\n<Id> <Total Price> <Issuer>"
                    + "\n\n" + "XXX"
                    + " \n\n\n Best regards, \n\n C# Pharmacy04";

                SmtpClient smtpClient = new SmtpClient("smtp.web.de", 587); //"smtp.gmail.com"
                smtpClient.Credentials = new System.Net.NetworkCredential()
                {
                    UserName = "pharmacy04@web.de",
                    Password = "pharmacy04ß?!z"
                };
                smtpClient.EnableSsl = true;
        try
			{
                ResultLabel.Text = null;
                smtpClient.Send(mailMessage);
                ResultLabel.Text = String.Format("Sending email to " + recipientEmail + " succeeded");
                ResultLabel.CssClass = "success";
			}
			catch (SmtpFailedRecipientsException ex)
			{
				for (int i = 0; i < ex.InnerExceptions.Length; i++)
				{
					SmtpStatusCode status = ex.InnerExceptions[i].StatusCode;
					if (status == SmtpStatusCode.MailboxBusy ||
						status == SmtpStatusCode.MailboxUnavailable)
					{
                        ResultLabel.Text = String.Format("Delivery failed - retrying in 5 seconds.");
                        ResultLabel.CssClass = "error";

					    //System.Diagnostics.Debug.Write("Delivery failed - retrying in 5 seconds.");
						System.Threading.Thread.Sleep(5000);
                        smtpClient.Send(mailMessage);
					}
					else
					{
                        ResultLabel.Text = String.Format("Failed to deliver message to " + recipientEmail);
                        ResultLabel.CssClass = "error";
                       // System.Diagnostics.Debug.Write("Failed to deliver message to " + recipientEmail, ex.InnerExceptions[i].FailedRecipient);
					}
				}
			}
            catch (Exception ex)
            {
                ResultLabel.Text = String.Format("Exception caught in RetryIfBusy(): " + ex);
                ResultLabel.CssClass = "error";
                //System.Diagnostics.Debug.Write("Exception caught in RetryIfBusy(): " + ex, ex.ToString() );
            }
        }



        private void Page_Error(object sender, EventArgs e)
        {
            Session["LastError"] = Server.GetLastError();
            Server.ClearError();
            Server.Transfer("~/Error.aspx");
        }
    }
}

/*
        protected void SendMailBtn_Click(object sender, EventArgs e)
        {
            try
            {
                
                String receiver = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Email;
                String recipientName = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Name;
                String recipientEmail = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Email;

                MailMessage mailMessage = new MailMessage("pharmacy04@web.de", receiver);
                mailMessage.Subject = "C# Pharmacy04 - Prescription Bill";
                mailMessage.Body = "Dear " + recipientName + ", \n\n Your overall prescription bill is: XXX" + "€." +
                    "\n\n Below you can check your prescription details: "
                    + "\n\n<Id> <Total Price> <Issuer>"
                    + "\n\n" + "XXX"
                    + " \n\n\n Best regards, \n\n C# Pharmacy04";

                SmtpClient smtpClient = new SmtpClient("smtp.web.de", 587); //"smtp.gmail.com"
                smtpClient.Credentials = new System.Net.NetworkCredential()
                {
                    UserName = "pharmacy04@web.de",
                    Password = "pharmacy04ß?!z"
                };

                smtpClient.EnableSsl = true;
                smtpClient.Send(mailMessage);

                System.Threading.Thread.Sleep(5000);

                ResultLabel.Text = String.Format("Sending email to " + recipientEmail + " succeeded");
                ResultLabel.CssClass = "success";
            }
            catch
            {
                String recipientEmail = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Email;
                ResultLabel.Text = String.Format("Sending email to " + recipientEmail + " failed");
                ResultLabel.CssClass = "error";
                //System.Diagnostics.Debug.Write("Sending email failed");
            }
        }
*/