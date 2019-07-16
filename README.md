[![CircleCI](https://circleci.com/gh/g4s8/ghman.svg?style=svg&circle-token=230db6a25558843ac4bb919dc955edad92531987)](https://circleci.com/gh/g4s8/ghman)
[![PDD status](http://www.0pdd.com/svg?name=g4s8/ghman)](http://www.0pdd.com/p?name=g4s8/ghman)

[![DevOps By Rultor.com](http://www.rultor.com/b/g4s8/ghman)](http://www.rultor.com/p/g4s8/ghman)
[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![Managed by Zerocrat](https://www.0crat.com/badge/CJT4R32Q3.svg)](https://www.0crat.com/p/CJT4R32Q3)
[![Help donate](https://www.0crat.com/contrib-badge/CJT4R32Q3.svg)](https://www.0crat.com/contrib/CJT4R32Q3)


# About

[@ghman_bot](https://t.me/ghman_bot) is a Telegram bot to work with Github notifications.
Bot can notify you on new notification, user can view new unread notifications,
put comments by replying to Telegram messages, mark notifications as read,
close tickets, merge PRs, etc. To start working you don't need to create GitHub auth tokens
manually, the bot is configured as GitHub application and it redirects user to GitHub OAuth page
to request permissions on first interraction.

# Usage

To start using the bot, just start talking with [@ghman_bot](https://t.me/ghman_bot)
via Telegram, the bot will ask you to authorize via Github using OAuth2,
on Github OAuth page you will be asked to grant access to your notifications.
After authorization you may start to use Telegram commands:
 - `/notifications` - to list all unread threads, you can click any
 thread to see new (unread) messages in the ticket or PR.
When you reply to Telegram message with a thread (from `/notifications`)
you're posting a comment to thread source (issue or PR), so you're
responding to a message right in Telegram bot.

# Contributing

If you're interested in contributing, you need to have an
account in [Zerocracy](https://www.0crat.com) and read this document:
https://github.com/g4s8/ghman/blob/master/CONTRIBUTING.md

If want to [submit a bug](https://www.zerocracy.com/policy.html#29) read
[this](https://www.yegor256.com/2018/02/06/where-to-find-more-bugs.html)
and
[this](https://www.yegor256.com/2018/04/24/right-way-to-report-bugs.html).

To propose some changes you need to be assigned as a performer (`DEV` role) to
some ticket. Then you have:
 1. Fork the repo, clone it locally
 2. Switch to branch for your issue, write a code, make sure it's building with
 `mvn clean install -Pqulice` command
 3. Submit a pull request, check that CI passed, fix errors if not passed
 4. Wait for code review, fix or discuss reviewer comments
 5. Wait for architect to merge (architect may ask additional changes)
 6. After merge to `master` ask issue author to close the ticket
 
Git guidelines:
 - start all commit message with tiket number, e.g.: `#1 - some message`
 - try to describe shortly your changes in commit message, long description
 should be provided in PR body
 - name your branches starting with ticket number, e.g. `1-some-bug` branch for #1 ticket
 - use `merge`, not `rebase` when merging changes from `master` to local branch
 - one commit per change, don't rebase all commits into single one for PR
 - avoid `push --force` where possible, it can be used in rare cases, e.g. if
 you pushed binary file by mistake, then you can remove it with `push --force`
 or if you mistyped ticket number or message, you can fix it with `push --force`
 
To build and run tests locally you need `>=jdk-8` and `>=maven-3.*`
