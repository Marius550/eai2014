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
                <span class="error">Telephone number required</span>
            </asp:RequiredFieldValidator>
        </p>
        <p class="label">Address</p>
        <p class="value">
            <asp:TextBox ID="AddressBox" runat="server" TextMode="MultiLine"></asp:TextBox>
        </p>
        <p class="form-footer">
            <asp:Button ID="SubmitBtn" runat="server" Text="Submit"
                onclick="SubmitBtn_Click" />
        </p>
    </form>
    <div class="result">
        <asp:Label ID="ResultLabel" runat="server"></asp:Label>
    </div>
</asp:Content>