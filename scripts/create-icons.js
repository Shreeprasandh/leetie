import fs from 'fs';
import path from 'path';

// Valid 1x1 base64 PNG data string
const base64Png = 'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==';
const buffer = Buffer.from(base64Png, 'base64');

const dirs = ['public/assets', 'src/assets'];

dirs.forEach((dir) => {
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true });
  }

  [16, 32, 48, 128].forEach((size) => {
    const filePath = path.join(dir, `icon-${size}.png`);
    fs.writeFileSync(filePath, buffer);
  });
});

console.log('Icon assets generated successfully.');
