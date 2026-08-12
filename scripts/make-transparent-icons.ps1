Add-Type -AssemblyName System.Drawing

$sizes = @(16, 32, 48, 128)
$targetDirs = @(
    "C:\Users\shree\shree_projects\leetie\public\assets",
    "C:\Users\shree\shree_projects\leetie\src\assets"
)

# Colors
$sageDark = [System.Drawing.ColorTranslator]::FromHtml("#84B179")
$sageMain = [System.Drawing.ColorTranslator]::FromHtml("#A2CB8B")

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
        $penDark = New-Object System.Drawing.Pen($sageDark, $penWidth)
        $penDark.StartCap = [System.Drawing.Drawing2D.LineCap]::Round
        $penDark.EndCap = [System.Drawing.Drawing2D.LineCap]::Round
        $penDark.LineJoin = [System.Drawing.Drawing2D.LineJoin]::Round

        $penMain = New-Object System.Drawing.Pen($sageMain, $penWidth)
        $penMain.StartCap = [System.Drawing.Drawing2D.LineCap]::Round
        $penMain.EndCap = [System.Drawing.Drawing2D.LineCap]::Round
        $penMain.LineJoin = [System.Drawing.Drawing2D.LineJoin]::Round

        # Left Bracket <
        $lb1 = New-Object System.Drawing.PointF(($size * 0.30), ($size * 0.32))
        $lb2 = New-Object System.Drawing.PointF(($size * 0.15), ($size * 0.50))
        $lb3 = New-Object System.Drawing.PointF(($size * 0.30), ($size * 0.68))
        $g.DrawLines($penDark, @($lb1, $lb2, $lb3))

        # Checkmark ✓ (in middle)
        $chk1 = New-Object System.Drawing.PointF(($size * 0.38), ($size * 0.52))
        $chk2 = New-Object System.Drawing.PointF(($size * 0.48), ($size * 0.66))
        $chk3 = New-Object System.Drawing.PointF(($size * 0.64), ($size * 0.34))
        $g.DrawLines($penMain, @($chk1, $chk2, $chk3))

        # Right Bracket >
        $rb1 = New-Object System.Drawing.PointF(($size * 0.70), ($size * 0.32))
        $rb2 = New-Object System.Drawing.PointF(($size * 0.85), ($size * 0.50))
        $rb3 = New-Object System.Drawing.PointF(($size * 0.70), ($size * 0.68))
        $g.DrawLines($penDark, @($rb1, $rb2, $rb3))

        $penDark.Dispose()
        $penMain.Dispose()
        $g.Dispose()

        $outPath = Join-Path $dir ("icon-$size.png")
        $bmp.Save($outPath, [System.Drawing.Imaging.ImageFormat]::Png)
        $bmp.Dispose()
    }
}

Write-Host "Pixel-perfect 100% TRANSPARENT sage icons generated successfully."
