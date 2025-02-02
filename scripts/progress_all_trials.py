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
@click.option(
    "-c",
    "--compare",
    type=str,
    help="Directory containing the comparison log files",
    default=None,
)
@click.pass_context
def show_progress_graph(
    ctx: click.Context,
    dir: str | None,
    num: int,
    lim: int,
    compare: str | None,
) -> None:
    # 色の設定
    original_color = "blue"
    compare_color = "orange"

    # 元の試行結果をプロット
    for i in range(num):
        filename = "logs/{}/{}/progress.csv".format(
            dir,
            i + 1,
        )
        print("Reading '{}'...".format(filename))
        if not os.path.exists(filename):
            print(f"File not found: {filename}")
            continue
        data = pd.read_csv(filename)

        plt.plot(
            data["evaluations_num"],
            data["best_fit"],
            color=original_color,
        )
        if lim is None:
            lim = data["evaluations_num"].max()

    # 比較試行結果をプロット
    if compare:
        for i in range(num):
            filename = "logs/{}/{}/progress.csv".format(
                compare,
                i + 1,
            )
            print("Reading comparison '{}'...".format(filename))
            if not os.path.exists(filename):
                print(f"Comparison file not found: {filename}")
                continue

            data = pd.read_csv(filename)
            plt.plot(
                data["evaluations_num"],
                data["best_fit"],
                color=compare_color,
            )

    # グラフ全体の設定
    plt.xlabel("The number of evaluations")
    plt.ylabel("Fitness")
    plt.xlim(1000, lim)
    plt.xscale("log")
    plt.ylim(0, 1)
    plt.grid(True)
    plt.legend(fontsize="small", loc="best")

    # 保存先を作成して保存
    fig_filename = "analytics/graph.png"
    os.makedirs(os.path.dirname(fig_filename), exist_ok=True)
    plt.tight_layout()
    plt.savefig(fig_filename)
    print(f"Graph saved to {fig_filename}")


if __name__ == "__main__":
    show_progress_graph()
