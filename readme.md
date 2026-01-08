# nicklausw's babashka scripts

To install the command line tools, run [install.bb](./install.bb) as sudo. Everything gets put in `/usr/local/bin`.

## scripts

### bulk-rename
```bash
bulk-rename path = ".", before, after
bulk-rename "bad tv show name episode (.*)\.mp4" "Proper TV Show Name S01E\$1.mp4"
```
