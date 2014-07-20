<%@ Page Title="" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true"
    CodeBehind="Error.aspx.cs" Inherits="WebLayer.Errors" %>
<asp:Content ID="Content1" ContentPlaceHolderID="ContentHolder" runat="server">
    <form id="form1" runat="server">
    Your request could not be processed.<br />
    <asp:Label ID="ErrorLabel" runat="server" CssClass="error"></asp:Label>
    </form>
</asp:Content>
