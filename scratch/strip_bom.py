import os

base_dir = r"c:\Users\Larissa\Programming_Languages\Jflex-Java"

# List of all entrada.txt files to check and strip BOM
files = [
    os.path.join(base_dir, "entrada.txt"),
    os.path.join(base_dir, "bin", "entrada.txt")
]
for i in range(1, 11):
    files.append(os.path.join(base_dir, "bin", f"roteiro_{i}", "bin", "entrada.txt"))

# Strip BOM
for fpath in files:
    if os.path.exists(fpath):
        with open(fpath, 'rb') as f:
            content = f.read()
        
        # UTF-8 BOM is b'\xef\xbb\xbf'
        if content.startswith(b'\xef\xbb\xbf'):
            cleaned = content[3:]
            with open(fpath, 'wb') as f:
                f.write(cleaned)
            print(f"BOM removido de: {fpath}")
        else:
            print(f"Sem BOM em: {fpath}")

print("BOM strip concluido!")
