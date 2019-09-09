<xsl:stylesheet version="1.0"	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Identity template -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="page">
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:apply-templates select="text">
        <xsl:sort order="ascending" data-type="number" select="@top"/>
		<xsl:sort order="ascending" data-type="number" select="@left"/>
      </xsl:apply-templates>      
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
