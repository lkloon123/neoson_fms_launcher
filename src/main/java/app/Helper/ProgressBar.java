package app.Helper;

/**
 * @author Lam Kai Loon <lkloon123@hotmail.com>
 */
public class ProgressBar {
    private StringBuilder progress;

    public ProgressBar() {
        init();
    }

    public void update(long done, long total) {
        char[] workchars = {'|', '/', '-', '\\'};
        String format = "\r%3d%% %s %c";

        long percent = (++done * 100) / total;
        long extrachars = (percent / 2) - this.progress.length();

        while (extrachars-- > 0) {
            progress.append('#');
        }

        System.out.printf(format, percent, progress,
                workchars[(int) (done % workchars.length)]);

        if (done >= total) {
            System.out.flush();
            System.out.println();
            init();
        }
    }

    private void init() {
        this.progress = new StringBuilder(60);
    }
}
