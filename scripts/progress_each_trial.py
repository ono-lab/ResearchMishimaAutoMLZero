import os
import matplotlib.pyplot as plt
import pandas as pd
import click


@click.command()
@click.option(
    "-d",
    "--dir",
    type=str,
    help="Directory containing the log files",
)
@click.option(
    "-p",
    "--prefix",
    type=str,
    help="Prefix for the log files",
    default="progress_",
)
@click.option(
    "-n",
    "--num",
    type=int,
    help="The number of experiments",
    default=10,
)
@click.option(
    "-l",
    "--lim",
    type=int,
    help="The number of evaluations to show",
    default=None,
)
@click.pass_context
def show_progress_graph(
    ctx: click.Context, dir: str | None, prefix: str | None, num: int, lim: int
) -> None:
    # サブプロットの作成
    ncols = 2  # 列数を2に設定
    nrows = (num + 1) // ncols  # 必要な行数を計算
    fig, axs = plt.subplots(nrows, ncols, figsize=(10, 15))
    axs = axs.flatten()  # 2D配列を1Dに変換してループしやすくする

    # グラフ全体で共通の凡例のためのプロット情報を格納
    lines = []
    labels = []

    # 各実験データを読み込んでプロット
    for i in range(num):
        filename = "logs/{}{}{}.csv".format(dir, prefix, i + 1)
        print("Reading '{}'...".format(filename))
        data = pd.read_csv(filename)

        # グラフをサブプロットにプロット
        ax = axs[i]
        (best_fit,) = ax.plot(
            data["evaluations_num"], data["best_fit"], color="blue", label="Best Fit"
        )
        (mean_fit,) = ax.plot(
            data["evaluations_num"], data["mean"], color="blue", alpha=0.5, label="Mean"
        )
        fill = ax.fill_between(
            data["evaluations_num"],
            data["mean"] - data["stdev"],
            data["mean"] + data["stdev"],
            color="blue",
            alpha=0.1,
            label="Standard Deviation",
        )
        ax.set_title("Experiment {}".format(i + 1))
        ax.set_xlabel("The number of evaluations")
        ax.set_ylabel("Best fitness")
        ax.set_ylim(0, 1)
        if lim is None:
            lim = data["evaluations_num"].max()
        ax.set_xlim(0, lim)
        ax.grid(True)

        # 最初のループで凡例の情報を取得
        if i == 0:
            lines.extend([best_fit, mean_fit, fill])
            labels.extend(
                [best_fit.get_label(), mean_fit.get_label(), "Standard Deviation"]
            )

    # 残りの空サブプロットを削除
    for j in range(i + 1, len(axs)):
        fig.delaxes(axs[j])

    # サブプロット間の隙間を調整
    plt.subplots_adjust(hspace=1, wspace=0.5)

    # 全体の凡例を追加
    fig.legend(lines, labels, loc="upper center", ncol=3, fontsize="large")

    fig_filename = "analytics/{}{}each.png".format(dir, prefix)
    os.makedirs(os.path.dirname(fig_filename), exist_ok=True)
    plt.tight_layout(rect=[0, 0, 1, 0.95])  # rectで凡例の領域を確保
    plt.savefig(fig_filename)
