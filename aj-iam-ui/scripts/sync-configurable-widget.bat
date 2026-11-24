@echo off
setlocal
set "widgetDir=%~dp0..\..\..\aj-smart-code-platform\aj-configurable-widget"

pushd "%widgetDir%" || exit /b 1
for /f "delims=" %%V in ('node -p "require('./package.json').version"') do set "widgetVersion=%%V"
if not defined widgetVersion goto :failed
set "archive=%CD%\target\ajax-js-configurable-widget-%widgetVersion%.tgz"
if not exist target mkdir target
call npm pack --pack-destination ./target || goto :failed
if not exist "%archive%" goto :failed
popd

pushd "%~dp0.." || exit /b 1
call npm install --no-save --package-lock=false --legacy-peer-deps --no-audit --no-fund "%archive%" || goto :failed
call npm run postinstall || goto :failed
call npm run build || goto :failed
popd
echo Configurable widget %widgetVersion% synchronized.
exit /b 0

:failed
popd
echo Configurable widget synchronization failed. 1>&2
exit /b 1
