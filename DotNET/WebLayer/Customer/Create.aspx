<%@ Page Title="C.Sharpe - Create Customer" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" 
        CodeBehind="Create.aspx.cs" Inherits="WebLayer.Customer.Create" %>
    <asp:Content ID="Content2" ContentPlaceHolderID="ContentHolder" runat="server">
    <form id="CustomerCreateForm" runat="server">
        <p class="label">Name</p>
        <p class="value">
            <asp:TextBox ID="NameBox" runat="server" />
            <asp:RequiredFieldValidator ID="NameBoxValidator" runat="server" 
                ControlToValidate="NameBox" ErrorMessage="RequiredFieldValidator"
                EnableClientScript="false">
                <span class="error">Name required</span>
            </asp:RequiredFieldValidator>
        </p>
        <p class="label">Telephone number</p>
        <p class="value">
            <asp:TextBox ID="TelephoneNumberBox" runat="server"></asp:TextBox>
            <asp:RequiredFieldValidator ID="TelephoneNumberBoxValidator" runat="server" 
                ControlToValidate="TelephoneNumberBox" ErrorMessage="RequiredFieldValidator"
                EnableClientScript="false">
                <span class="error">Telephone number required!</span>
            </asp:RequiredFieldValidator>
        <asp:RegularExpressionValidator ID="RegularTelephoneNumber" ControlToValidate="TelephoneNumberBox" 
            EnableClientScript="false" runat="server"
            ValidationExpression="^[0-9]{12}$">
            <span class="error">Please use the common form (E.g.: 12 digit number: 017912345678).</span>
        </asp:RegularExpressionValidator>
        </p>
        
        <p class="label">Address</p>
        <p class="value">
            <asp:TextBox ID="AddressBox" runat="server" TextMode="MultiLine"></asp:TextBox>
        </p>

        <p class="label">Email</p>
        <p class="value">
            <asp:TextBox ID="EmailBox" runat="server" TextMode="MultiLine"></asp:TextBox>
            <asp:RequiredFieldValidator ID="EmailBoxValidator" runat="server" 
                ControlToValidate="EmailBox" ErrorMessage="RequiredFieldValidator"
                EnableClientScript="false">
                <span class="error">Email required!</span>
            </asp:RequiredFieldValidator>
        <asp:RegularExpressionValidator ID="RequiredEmail" ControlToValidate="EmailBox" 
            EnableClientScript="false" runat="server"
            ValidationExpression="^([0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,9})$">
            <span class="error">Please use the common form (E.g.: name@example.com).</span>
        </asp:RegularExpressionValidator>
        </p>

        <p class="label">Birth date</p>
        <p class="value">
            <asp:TextBox ID="BirthDateBox" TextMode="DateTime" runat="server" />
            <asp:RequiredFieldValidator ID="RequiredBirthDate" ControlToValidate="BirthDateBox" EnableClientScript="false" runat="server">
                <span class="error">Birth date required </span>
            </asp:RequiredFieldValidator>
            <asp:RegularExpressionValidator ID="ValidBirthDate" ControlToValidate="BirthDateBox" 
            EnableClientScript="false" runat="server"
            ValidationExpression="\d{2}.\d{2}.\d{4}">
            <span class="error">Please use the common form (E.g.: 24.12.2014). </span>
        </asp:RegularExpressionValidator>
        </p>

        <p class="form-footer">
            <asp:Button ID="SubmitBtn" runat="server" Text="Submit" onclick="SubmitBtn_Click" />
        </p>
    </form>

    <div class="result">
        <asp:Label ID="ResultLabel" runat="server"></asp:Label>
    </div>
</asp:Content>

