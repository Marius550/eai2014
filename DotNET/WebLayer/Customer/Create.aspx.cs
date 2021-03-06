﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebLayer.Customer
{
    public partial class Create : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void SubmitBtn_Click(object sender, EventArgs e)
        {
            if (!Page.IsValid)
                return;
            try
            {
                    string birthDateString = ((TextBox)CustomerCreateForm.FindControl("BirthDateBox")).Text;
                    DateTime birthDate = Util.ParseDate(birthDateString);

                    Pharmacy.BusinessLayer.Data.Customer result =
                        Pharmacy.BusinessLayer.Logic.CustomerService.CreateCustomer(NameBox.Text, TelephoneNumberBox.Text, AddressBox.Text, EmailBox.Text, birthDate);
                    
                    ResultLabel.Text = String.Format("Customer '{0}' created.", result.Name);
                    ResultLabel.CssClass = "success";

                    NameBox.Text = "";
                    TelephoneNumberBox.Text = "";
                    AddressBox.Text = "";
                    EmailBox.Text = "";
                    BirthDateBox.Text = "";
            }
            catch (ArgumentException ex)
            {
                ResultLabel.Text = String.Format("Customer not created: {0}", ex.Message);
                ResultLabel.CssClass = "error";
            }
        }
    }
}

/*
    <div class="result">
        <asp:Label ID="ResultLabelPhoneValidation" runat="server"></asp:Label>
    </div>
    <div class="result">
        <asp:Label ID="ResultLabelEmailValidation" runat="server"></asp:Label>
    </div>
 */

        /*
        protected void RegexEmail ()
        {
            Regex regex = new Regex(@"^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$");

            if (!regex.IsMatch(EmailBox.Text))
            {
                evaluationEmail = false;
            }
            else
            {
                evaluationEmail = true;
            }
        }

        protected void RegexPhone()
        {
            Regex regex = new Regex("^[0-9]+(-[0-9]+)*$");
            if (!regex.IsMatch(TelephoneNumberBox.Text))
            {
                evaluationPhone = false;
            }
            else
            {
                evaluationPhone = true;
            }
        }
         */

/*
protected string EmailValidator()
{
    return "Please use the common form (E.g.: name@example.com). " + RegexEmail();
 * <span><%= EmailValidator() %></span>
}

protected string TelephoneValidator()
{
    return "Please use the common form (International, e.g.: 492518338250). " + RegexPhone();
    <span><%= TelephoneValidator() %></span>
}
 */

/*
<asp:RequiredFieldValidator ID="EmailBoxValidator2" runat="server" 
        ControlToValidate="EmailBox" ErrorMessage="RequiredFieldValidator"
        EnableClientScript="false">
        <span class="error">Email required</span>
    </asp:RequiredFieldValidator>
 */

/*
    <asp:RequiredFieldValidator ID="EmailBoxValidator" runat="server" ControlToValidate="EmailBox" ErrorMessage="RequiredFieldValidator"
        ValidationExpression="^([0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,9})$" 
        EnableClientScript="false"> 
        <span class="error">Email required</span> 
    </asp:RequiredFieldValidator>
*/

//ResultLabelEmailValidation.Text = null;
//ResultLabelPhoneValidation.Text = null;

//RegexPhone();
//RegexEmail();
//if (evaluationPhone == true && evaluationEmail == true)
//{

//}
//else
//{
//Hint: Make use of the "Result.Label"!
//ResultLabel.Text = String.Format("Customer created: " + NameBox.Text);
//ResultLabel.CssClass = "success";

/*
    if (evaluationEmail == false && evaluationPhone == false)
    {
    ResultLabelEmailValidation.Text = String.Format("Email format is not correct");
    ResultLabelEmailValidation.CssClass = "error";
    ResultLabelPhoneValidation.Text = String.Format("Telephone number format is not correct");
    ResultLabelPhoneValidation.CssClass = "error";
    }
    else if (evaluationEmail == false && evaluationPhone == true)
    {
    ResultLabelEmailValidation.Text = String.Format("Email format is not correct");
    ResultLabelEmailValidation.CssClass = "error";
    }
    else if (evaluationEmail == true && evaluationPhone == false)
    {
    ResultLabelPhoneValidation.Text = String.Format("Telephone number format is not correct");
    ResultLabelPhoneValidation.CssClass = "error";
    }
}
 */