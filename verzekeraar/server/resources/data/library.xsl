<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- Edited by XMLSpy® -->
<xsl:stylesheet version="1.0" xmlns="http://www.w3.org/1999/xhtml">
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template> 
  <html>
  <title>Library XSLT example</title>
  <body>
    <h2>Library Information</h2>
    <table border="1">
      <tr bgcolor="#9acd32">
        <th>Title</th>
        <th>Artist</th>
      </tr>
      <xsl:for-each select="Library/Member">
      <tr>
        <td><xsl:value-of select="Firstname"/></td>
        <td><xsl:value-of select="Lastname"/></td>
      </tr>
      </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>

