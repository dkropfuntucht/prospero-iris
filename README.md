# Iris

```
Where thou thyself dost air;—the queen o’ the sky,
Whose watery arch and messenger am I,
Bids thee leave these; and with her sovereign grace,
Here on this grass-plot, in this very place,
To come and sport:—her peacocks fly amain:
```
- Iris - _The Tempest_

## What this is

A React and DOM game engine pluggable renderer for [Prospero](https://github.com/dkropfuntucht/prospero).

## Usage

Pull Iris into your Prospero project to use a DOM based renderer.

```
["prospero/iris" "0.1.0"]
```

Specify Iris as the game-system for Prospero with:

```
{:prospero.core/game-system  :iris.core/game-system
 :display-width              1024
 :display-height             768
 :iris.core/web-root         (.getElementById js/document "app")}
 ```

## License

Copyright © 2020 Damon Kropf-Untucht

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
