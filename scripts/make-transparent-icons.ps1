Add-Type -AssemblyName System.Drawing

$sizes = @(16, 32, 48, 128)
$targetDirs = @(
    "C:\Users\shree\shree_projects\leetie\public\assets",
    "C:\Users\shree\shree_projects\leetie\src\assets"
)

# Colors matching custom palette #DDDAD0 and #F8F3CE
$parchment = [System.Drawing.ColorTranslator]::FromHtml("#DDDAD0")
$cream = [System.Drawing.ColorTranslator]::FromHtml("#F8F3CE")

foreach ($dir in $targetDirs) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force
    }
    foreach ($size in $sizes) {
        $bmp = New-Object System.Drawing.Bitmap($size, $size, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
        $g = [System.Drawing.Graphics]::FromImage($bmp)
        $g.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
        $g.Clear([System.Drawing.Color]::Transparent)

        $penWidth = [Math]::Max(2, [Math]::Round($size * 0.12))
        $penParchment = New-Object System.Drawing.Pen($parchment, $penWidth)
        $penParchment.StartCap = [System.Drawing.Drawing2D.LineCap]::Round
        $penParchment.EndCap = [System.Drawing.Drawing2D.LineCap]::Round
        $penParchment.LineJoin = [System.Drawing.Drawing2D.LineJoin]::Round

        $penCream = New-Object System.Drawing.Pen($cream, $penWidth)
        $penCream.StartCap = [System.Drawing.Drawing2D.LineCap]::Round
        $penCream.EndCap = [System.Drawing.Drawing2D.LineCap]::Round
        $penCream.LineJoin = [System.Drawing.Drawing2D.LineJoin]::Round

        # Left Bracket <
        $lb1 = New-Object System.Drawing.PointF(($size * 0.30), ($size * 0.32))
        $lb2 = New-Object System.Drawing.PointF(($size * 0.15), ($size * 0.50))
        $lb3 = New-Object System.Drawing.PointF(($size * 0.30), ($size * 0.68))
        $g.DrawLines($penParchment, @($lb1, $lb2, $lb3))

        # Checkmark ✓ (in middle)
        $chk1 = New-Object System.Drawing.PointF(($size * 0.38), ($size * 0.52))
        $chk2 = New-Object System.Drawing.PointF(($size * 0.48), ($size * 0.66))
        $chk3 = New-Object System.Drawing.PointF(($size * 0.64), ($size * 0.34))
        $g.DrawLines($penCream, @($chk1, $chk2, $chk3))

        # Right Bracket >
        $rb1 = New-Object System.Drawing.PointF(($size * 0.70), ($size * 0.32))
        $rb2 = New-Object System.Drawing.PointF(($size * 0.85), ($size * 0.50))
        $rb3 = New-Object System.Drawing.PointF(($size * 0.70), ($size * 0.68))
        $g.DrawLines($penParchment, @($rb1, $rb2, $rb3))

        $penParchment.Dispose()
        $penCream.Dispose()
        $g.Dispose()

        $outPath = Join-Path $dir ("icon-$size.png")
        $bmp.Save($outPath, [System.Drawing.Imaging.ImageFormat]::Png)
        $bmp.Dispose()
    }
}

Write-Host "Warm palette transparent PNG icons generated successfully."
