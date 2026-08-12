Add-Type -AssemblyName System.Drawing

$srcPath = "C:\Users\shree\shree_projects\leetie\public\assets\logo.jpg"
$srcImg = [System.Drawing.Image]::FromFile($srcPath)
$sizes = @(16, 32, 48, 128)
$targetDirs = @(
    "C:\Users\shree\shree_projects\leetie\public\assets",
    "C:\Users\shree\shree_projects\leetie\src\assets"
)

foreach ($dir in $targetDirs) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force
    }
    foreach ($size in $sizes) {
        $bmp = New-Object System.Drawing.Bitmap($size, $size)
        $g = [System.Drawing.Graphics]::FromImage($bmp)
        $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
        $g.DrawImage($srcImg, 0, 0, $size, $size)
        $g.Dispose()
        $outPath = Join-Path $dir ("icon-$size.png")
        $bmp.Save($outPath, [System.Drawing.Imaging.ImageFormat]::Png)
        $bmp.Dispose()
    }
}

$srcImg.Dispose()
Write-Host "Icons generated from emerald logo successfully."
