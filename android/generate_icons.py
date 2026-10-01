import os
from PIL import Image, ImageDraw

def make_round(img):
    min_dim = min(img.size)
    img = img.crop((0, 0, min_dim, min_dim))
    mask = Image.new('L', img.size, 0)
    draw = ImageDraw.Draw(mask)
    draw.ellipse((0, 0, min_dim, min_dim), fill=255)
    result = img.copy()
    result.putalpha(mask)
    return result

source_path = r"D:\ClassMode - Do not disturb\Playstore_assets\icon.png"
res_dir = r"D:\ClassMode - Do not disturb\android\app\src\main\res"
playstore_dir = r"D:\ClassMode - Do not disturb\Playstore_assets"

sizes = {
    'mdpi': 48,
    'hdpi': 72,
    'xhdpi': 96,
    'xxhdpi': 144,
    'xxxhdpi': 192
}

try:
    with Image.open(source_path) as img:
        img = img.convert("RGBA")
        img.resize((512, 512), Image.Resampling.LANCZOS).save(os.path.join(playstore_dir, "play_store_512.png"))
        img.resize((1024, 1024), Image.Resampling.LANCZOS).save(os.path.join(playstore_dir, "play_store_1024.png"))
        
        for density, size in sizes.items():
            folder = os.path.join(res_dir, f"mipmap-{density}")
            os.makedirs(folder, exist_ok=True)
            
            square_img = img.resize((size, size), Image.Resampling.LANCZOS)
            square_img.save(os.path.join(folder, "ic_launcher.png"))
            
            round_img = make_round(square_img)
            round_img.save(os.path.join(folder, "ic_launcher_round.png"))
            
            fg_size = int(108 * (size / 48))
            fg_img = img.resize((fg_size, fg_size), Image.Resampling.LANCZOS)
            fg_img.save(os.path.join(folder, "ic_launcher_foreground.png"))
            
    print("Success")
except Exception as e:
    print(f"Error: {e}")