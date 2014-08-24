using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Text.RegularExpressions;

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
                Pharmacy.BusinessLayer.Data.Customer result =
                    Pharmacy.BusinessLayer.Logic.CustomerService.CreateCustomer(NameBox.Text, TelephoneNumberBox.Text, AddressBox.Text, EmailBox.Text);
                ResultLabel.Text = String.Format("Customer '{0}' created.", result.Name);
                ResultLabel.CssClass = "success";
                NameBox.Text = "";
                TelephoneNumberBox.Text = "";
                AddressBox.Text = "";
                EmailBox.Text = "";

                RegexPhone();
                RegexEmail();
            }
            catch (ArgumentException ex)
            {
                ResultLabel.Text = String.Format("Customer not created: {0}", ex.Message);
                ResultLabel.CssClass = "error";
            }
        }

        protected String RegexEmail ()
        {
            Regex regex = new Regex(@"^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$");
            Boolean eval;
            String evaluationString = "";
            if (!regex.IsMatch(EmailBox.Text))
            {
                eval = false;
                evaluationString = "Please fill in correctly";
            }
            else
            {
                eval = true;
                evaluationString = "Email format is correct";
            }
            return String.Concat(evaluationString, eval);
        }

        protected String RegexPhone()
        {
            Regex regex = new Regex("^[0-9]+(-[0-9]+)*$");
            Boolean eval;
            String evaluationString = "";
            if (!regex.IsMatch(TelephoneNumberBox.Text))
            {
                eval = false;
                evaluationString = "Please fill in correctly";
            }
            else
            {
                eval = true;
                evaluationString = "Telephone number format is correct";
            }
            return String.Concat(evaluationString, eval);
        }

        protected string EmailValidator()
        {
            return "Please use the common form (E.g.: name@example.com). " + RegexEmail();
        }

        protected string TelephoneValidator()
        {
            return "Please use the common form (International, e.g.: 492518338250). " + RegexPhone();
            //<asp:Label runat="server" Text='<%# ManipulateLabel("Information") %>'></asp:Label>
        }
    }
}

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