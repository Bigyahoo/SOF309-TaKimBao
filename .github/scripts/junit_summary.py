import glob
import os
import sys
import xml.etree.ElementTree as ET


def first_non_empty_line(text: str) -> str:
    for line in (text or "").splitlines():
        stripped = line.strip()
        if stripped:
            return stripped
    return ""


def main() -> int:
    title = sys.argv[1] if len(sys.argv) > 1 else "JUnit Test Results"
    summary_path = os.environ.get("GITHUB_STEP_SUMMARY")
    if not summary_path:
        print("GITHUB_STEP_SUMMARY is not set; skipping summary output.", file=sys.stderr)
        return 0

    files = sorted(glob.glob("target/surefire-reports/TEST-*.xml"))

    with open(summary_path, "a", encoding="utf-8") as out:
        out.write(f"## {title}\n\n")
        if not files:
            out.write("No JUnit XML reports found in `target/surefire-reports/`.\n\n")
            return 0

        totals = {"tests": 0, "failures": 0, "errors": 0, "skipped": 0, "time": 0.0}
        rows = []
        failures = []

        for path in files:
            root = ET.parse(path).getroot()
            name = root.attrib.get("name", os.path.basename(path))
            tests = int(root.attrib.get("tests", 0))
            fail = int(root.attrib.get("failures", 0))
            err = int(root.attrib.get("errors", 0))
            skip = int(root.attrib.get("skipped", 0))
            time_s = float(root.attrib.get("time", 0.0))
            passed = tests - fail - err - skip

            totals["tests"] += tests
            totals["failures"] += fail
            totals["errors"] += err
            totals["skipped"] += skip
            totals["time"] += time_s

            status = "Passed" if (fail == 0 and err == 0) else "Failed"
            rows.append(
                f"| `{name}` | {tests} | {passed} | {fail} | {err} | {skip} | {time_s:.2f}s | {status} |"
            )

            for tc in root.findall("testcase"):
                for tag in ("failure", "error"):
                    el = tc.find(tag)
                    if el is not None:
                        tc_name = f"{tc.attrib.get('classname', name)}.{tc.attrib.get('name', '')}"
                        msg = el.attrib.get("message", "").strip() or first_non_empty_line(el.text or "")
                        failures.append(f"- **{tc_name}** ({tag}): `{msg}`")

        total_passed = totals["tests"] - totals["failures"] - totals["errors"] - totals["skipped"]
        out.write(
            f"**Total:** {totals['tests']} | **Passed:** {total_passed} | "
            f"**Failed:** {totals['failures']} | **Errors:** {totals['errors']} | "
            f"**Skipped:** {totals['skipped']} | **Time:** {totals['time']:.2f}s\n\n"
        )
        out.write("| Test Suite | Tests | Passed | Failures | Errors | Skipped | Time | Status |\n")
        out.write("|---|---:|---:|---:|---:|---:|---:|---|\n")
        out.write("\n".join(rows) + "\n\n")

        if failures:
            out.write("### Failures / Errors\n\n")
            out.write("\n".join(failures) + "\n\n")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
